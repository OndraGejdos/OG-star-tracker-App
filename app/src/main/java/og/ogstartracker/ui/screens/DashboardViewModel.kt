package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import og.ogstartracker.Config.CAPTURING_INITIAL_DELAY
import og.ogstartracker.Config.SLEW_MAX_VALUE
import og.ogstartracker.Config.SLEW_MIN_VALUE
import og.ogstartracker.domain.events.PhotoControlEvent
import og.ogstartracker.domain.events.SlewControlEvent
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.arduino.StartCaptureUseCase
import og.ogstartracker.domain.usecases.providers.DashboardUseCaseProvider
import og.ogstartracker.domain.usecases.settings.SetNewSettingsUseCase
import og.ogstartracker.domain.usecases.settings.SettingItem
import og.ogstartracker.ui.components.common.input.NotEmptyValidator
import og.ogstartracker.ui.components.common.input.TextFieldState
import og.ogstartracker.utils.VibratorController
import og.ogstartracker.utils.WhileUiSubscribed
import og.ogstartracker.utils.onSuccess
import og.ogstartracker.utils.vibrationPatternClick
import og.ogstartracker.utils.vibrationPatternThreeClick
import timber.log.Timber

private const val SECOND = 1000L

class DashboardViewModel internal constructor(
	private val vibratorController: VibratorController,
	private val useCases: DashboardUseCaseProvider,
) : ViewModel() {

	private var timerJob: Job? = null

	val settingsItemsFlow = og.ogstartracker.utils.combine(
		useCases.getSettings(SettingItem.PIXEL_SIZE),
		useCases.getSettings(SettingItem.FOCAL_LENGTH),
		useCases.getSettings(SettingItem.EXPOSURE_COUNT),
		useCases.getSettings(SettingItem.EXPOSURE_TIME),
		useCases.getSettings(SettingItem.DITHER_ACTIVE),
		useCases.getSettings(SettingItem.SLEW_SPEED),
	) { pixelSize, focalLength, exposureCount, exposureTime, ditherActive, slewSpeed ->
		_uiState.update {
			it.copy(
				exposeTime = TextFieldState(text = exposureTime?.toString() ?: "", NotEmptyValidator()),
				frameCount = TextFieldState(text = exposureCount?.toString() ?: "", NotEmptyValidator()),
				ditherFocalLength = TextFieldState(text = focalLength?.toString() ?: "", NotEmptyValidator()),
				ditherPixelSize = TextFieldState(text = pixelSize?.toString() ?: "", NotEmptyValidator()),
				slewValue = slewSpeed ?: 0,
				ditheringEnabled = ditherActive == 1
			)
		}
		return@combine
	}
		.stateIn(viewModelScope, WhileUiSubscribed, Unit)

	private val _uiState = MutableStateFlow(DashboardUiState())
	val uiState = combine(
		useCases.getCurrentHemisphereFlow(),
		_uiState,
		useCases.didUserSeeOnboarding(),
		useCases.getLastArduinoMessage()
	) { hemisphere, uiState, userSawOnboarding, lastMessage ->
		uiState.copy(
			hemisphere = hemisphere,
			shouldShowOnboardingDialog = !userSawOnboarding,
			lastMessage = lastMessage
		)
	}.stateIn(viewModelScope, WhileUiSubscribed, DashboardUiState())

	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	internal fun changeSidereal(active: Boolean) {
		if (active) {
			sendCommand {
				useCases.startSiderealTracking(uiState.value.hemisphere ?: return@sendCommand).onSuccess {
					vibratorController.startVibrations(vibrationPatternThreeClick)
				}
				_uiState.update { it.copy(siderealActive = true) }
			}
		} else {
			sendCommand {
				useCases.stopSiderealTracking().onSuccess {
					vibratorController.startVibrations(vibrationPatternClick)
				}
				_uiState.update { it.copy(siderealActive = false) }
			}
		}
	}

	internal fun slewControlEvent(slewControlEvent: SlewControlEvent) {
		when (slewControlEvent) {
			SlewControlEvent.Minus -> {
				val newValue = (uiState.value.slewValue - 1).coerceAtLeast(SLEW_MIN_VALUE)
				_uiState.update { it.copy(slewValue = newValue) }
				vibratorController.startVibrations(vibrationPatternClick)
				notifyAboutChange(SettingItem.SLEW_SPEED, newValue)
			}

			SlewControlEvent.Plus -> {
				val newValue = (uiState.value.slewValue + 1).coerceAtMost(SLEW_MAX_VALUE)
				_uiState.update { it.copy(slewValue = newValue) }
				vibratorController.startVibrations(vibrationPatternClick)
				notifyAboutChange(SettingItem.SLEW_SPEED, newValue)
			}

			SlewControlEvent.RotateAnticlockwise -> {
				sendCommand {
					useCases.trackerLeft(uiState.value.slewValue).onSuccess {
						vibratorController.startVibrations(vibrationPatternClick)
					}
				}
			}

			SlewControlEvent.RotateClockwise -> {
				sendCommand {
					useCases.trackerRight(uiState.value.slewValue).onSuccess {
						vibratorController.startVibrations(vibrationPatternClick)
					}
				}
			}
		}
	}

	internal fun photoControlEvent(photoControlEvent: PhotoControlEvent) {
		when (photoControlEvent) {
			is PhotoControlEvent.DitheringActivation -> {
				_uiState.update {
					it.copy(
						ditheringEnabled = photoControlEvent.active,
						captureCount = 0,
						captureElapsedTimeMillis = 0,
						captureEstimatedTimeMillis = 0
					)
				}
				if (photoControlEvent.active) {
					vibratorController.startVibrations(vibrationPatternThreeClick)
				} else {
					vibratorController.startVibrations(vibrationPatternClick)
				}
			}

			PhotoControlEvent.EndCapture -> {
				sendCommand {
					useCases.abortCapture().onSuccess {
						vibratorController.startVibrations(vibrationPatternClick)
						_uiState.update {
							it.copy(
								capturingActive = false,
								captureCount = 0,
								captureElapsedTimeMillis = 0,
								captureEstimatedTimeMillis = 0
							)
						}
						stopTimer()
					}
				}
			}

			PhotoControlEvent.StartCapture -> {
				sendCommand {
					useCases.startCapture(
						StartCaptureUseCase.Input(
							exposure = uiState.value.exposeTime.textState.text.toIntOrNull() ?: return@sendCommand,
							numExposures = uiState.value.frameCount.textState.text.toIntOrNull() ?: return@sendCommand,
							focalLength = if (uiState.value.ditheringEnabled) {
								uiState.value.ditherFocalLength.textState.text.toIntOrNull() ?: return@sendCommand
							} else {
								0
							},
							pixSize = if (uiState.value.ditheringEnabled) {
								uiState.value.ditherPixelSize.textState.text.toIntOrNull() ?: return@sendCommand
							} else {
								0
							},
							ditherEnabled = if (uiState.value.ditheringEnabled) 1 else 0,
						)
					).onSuccess {
						_uiState.update { it.copy(capturingActive = true, captureStartTime = System.currentTimeMillis()) }
						startTimer()
						vibratorController.startVibrations(vibrationPatternThreeClick)
					}
				}

			}
		}
	}

	private fun sendCommand(command: suspend () -> Unit) {
		viewModelScope.launch(Dispatchers.Default) {
			command()
		}
	}

	private fun startTimer() {
		timerJob?.cancel()

		val exposureTime = uiState.value.exposeTime.textState.text.toIntOrNull() ?: return
		val frameCount = uiState.value.frameCount.textState.text.toIntOrNull() ?: return

		timerJob = viewModelScope.launch(Dispatchers.Default) {
			_uiState.update { it.copy(captureEstimatedTimeMillis = (exposureTime * frameCount * SECOND)) }
			delay(CAPTURING_INITIAL_DELAY)

			val captureStarTime = System.currentTimeMillis()

			while (exposureTime * frameCount > ((System.currentTimeMillis() - captureStarTime) / SECOND)) {
				delay(exposureTime * SECOND)
				_uiState.update {
					it.copy(
						captureCount = (it.captureCount ?: 0) + 1,
						captureElapsedTimeMillis = System.currentTimeMillis() - captureStarTime,
					)
				}
			}

			_uiState.update {
				it.copy(
					captureCount = 0,
					captureElapsedTimeMillis = 0,
					captureEstimatedTimeMillis = 0
				)
			}

			stopTimer()
			photoControlEvent(PhotoControlEvent.EndCapture)
		}
	}

	private fun stopTimer() {
		timerJob?.cancel()
	}

	internal fun notifyAboutChange(settingItem: SettingItem, value: Int?) {
		Timber.d("notifyAboutChange, settingItem: $settingItem, value: $value")
		viewModelScope.launch(Dispatchers.Default) {
			useCases.setNewSettings(SetNewSettingsUseCase.Input(settingItem, value))
		}
	}

	internal fun setUserSawOnboard() {
		viewModelScope.launch(Dispatchers.Default) {
			useCases.setUserSawOnboarding()
		}
	}

	internal fun setHaveLocationPermission(active: Boolean) {
		_uiState.update { it.copy(haveLocationPermission = active) }
	}

	internal fun setConnection(connected: Boolean) {
		_uiState.update { it.copy(trackerConnected = connected) }
	}

	internal fun resetMessage() {
		viewModelScope.launch(Dispatchers.Default) {
			useCases.resetLastArduinoMessage()
		}
	}
}

data class DashboardUiState internal constructor(
	val hemisphere: Hemisphere? = null,
	val trackerConnected: Boolean = false,
	val haveLocationPermission: Boolean = false,
	val openedCheckbox: Boolean = false,
	val siderealActive: Boolean = false,
	val ditheringEnabled: Boolean = false,
	val capturingActive: Boolean = false,
	val lastMessage: String? = null,
	val slewValue: Int = 0,
	val captureStartTime: Long? = null,
	val captureCount: Int? = null,
	val captureElapsedTimeMillis: Long? = null,
	val captureEstimatedTimeMillis: Long? = null,
	val shouldShowOnboardingDialog: Boolean = false,
	val exposeTime: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val frameCount: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val ditherFocalLength: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val ditherPixelSize: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
) {
	fun getCaptureRatio() = buildString {
		append(captureCount ?: 0)
		append("/")
		append(frameCount.textState.text.toIntOrNull() ?: 0)
	}

	fun arePhotoControlInputsValid(): Boolean {
		val intervalometerValid = exposeTime.isValid() && frameCount.isValid()
		val ditherValid = ditherFocalLength.isValid() && ditherPixelSize.isValid()

		return if (ditheringEnabled) {
			ditherValid && intervalometerValid
		} else {
			intervalometerValid
		}
	}
}