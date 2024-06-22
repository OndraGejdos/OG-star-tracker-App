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
import og.ogstartracker.Config.SLEW_MAX_VALUE
import og.ogstartracker.Config.SLEW_MIN_VALUE
import og.ogstartracker.domain.events.PhotoControlEvent
import og.ogstartracker.domain.events.SlewControlEvent
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.AbortCaptureUseCase
import og.ogstartracker.domain.usecases.GetCurrentHemisphereFlowUseCase
import og.ogstartracker.domain.usecases.StartCaptureUseCase
import og.ogstartracker.domain.usecases.StartSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.StopSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.TurnTrackerLeftUseCase
import og.ogstartracker.domain.usecases.TurnTrackerRightUseCase
import og.ogstartracker.ui.components.common.input.NotEmptyValidator
import og.ogstartracker.ui.components.common.input.TextFieldState
import og.ogstartracker.utils.VibratorController
import og.ogstartracker.utils.WhileUiSubscribed
import og.ogstartracker.utils.onSuccess
import og.ogstartracker.utils.vibrationPatternClick
import og.ogstartracker.utils.vibrationPatternThreeClick

class DashboardViewModel internal constructor(
	private val vibratorController: VibratorController,
	private val startSiderealTracking: StartSiderealTrackingUseCase,
	private val stopSiderealTracking: StopSiderealTrackingUseCase,
	private val trackerLeft: TurnTrackerLeftUseCase,
	private val trackerRight: TurnTrackerRightUseCase,
	private val startCapture: StartCaptureUseCase,
	private val abortCapture: AbortCaptureUseCase,
	getCurrentHemisphereFlow: GetCurrentHemisphereFlowUseCase,
) : ViewModel() {

	private var timerJob: Job? = null

	private val _uiState = MutableStateFlow(DashboardUiState())
	val uiState = combine(
		getCurrentHemisphereFlow(),
		_uiState
	) { hemisphere, uiState ->
		uiState.copy(hemisphere = hemisphere)
	}.stateIn(viewModelScope, WhileUiSubscribed, DashboardUiState())

	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	internal fun changeSidereal(active: Boolean) {
		if (active) {
			sendCommand {
				startSiderealTracking(uiState.value.hemisphere ?: return@sendCommand).onSuccess {
					vibratorController.startVibrations(vibrationPatternThreeClick)
				}
			}
		} else {
			sendCommand {
				stopSiderealTracking().onSuccess {
					vibratorController.startVibrations(vibrationPatternClick)
				}
			}
		}

		_uiState.update { it.copy(siderealActive = active) }
	}

	fun slewControlEvent(slewControlEvent: SlewControlEvent) {
		when (slewControlEvent) {
			SlewControlEvent.Minus -> {
				_uiState.update {
					it.copy(slewValue = (it.slewValue - 1).coerceAtLeast(SLEW_MIN_VALUE))
				}
				vibratorController.startVibrations(vibrationPatternClick)
			}

			SlewControlEvent.Plus -> {
				_uiState.update {
					it.copy(slewValue = (it.slewValue + 1).coerceAtMost(SLEW_MAX_VALUE))
				}
				vibratorController.startVibrations(vibrationPatternClick)
			}

			SlewControlEvent.RotateAnticlockwise -> {
				sendCommand {
					trackerLeft(uiState.value.slewValue).onSuccess {
						vibratorController.startVibrations(vibrationPatternClick)
					}
				}
			}

			SlewControlEvent.RotateClockwise -> {
				sendCommand {
					trackerRight(uiState.value.slewValue).onSuccess {
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
					it.copy(ditheringEnabled = photoControlEvent.active, captureCount = -1, captureElapsedTimeMillis = 0)
				}
				if (photoControlEvent.active) {
					vibratorController.startVibrations(vibrationPatternThreeClick)
				} else {
					vibratorController.startVibrations(vibrationPatternClick)
				}
			}

			PhotoControlEvent.EndCapture -> {
				sendCommand {
					abortCapture().onSuccess {
						vibratorController.startVibrations(vibrationPatternClick)
						_uiState.update { it.copy(capturingActive = false, captureCount = 0, captureElapsedTimeMillis = 0) }
						stopTimer()
					}
				}
			}

			PhotoControlEvent.StartCapture -> {
				sendCommand {
					startCapture(
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
			delay(3000)

			val captureStarTime = System.currentTimeMillis()

			while (exposureTime * frameCount > ((System.currentTimeMillis() - captureStarTime) / 1000)) {
				delay(exposureTime * 1000L)
				_uiState.update {
					it.copy(
						captureCount = (it.captureCount ?: 0) + 1,
						captureElapsedTimeMillis = System.currentTimeMillis() - captureStarTime
					)
				}
			}

			_uiState.update { it.copy(captureCount = 0, captureElapsedTimeMillis = 0) }

			stopTimer()
			photoControlEvent(PhotoControlEvent.EndCapture)
		}
	}

	private fun stopTimer() {
		timerJob?.cancel()
	}
}

data class DashboardUiState internal constructor(
	val hemisphere: Hemisphere? = null,
	val trackerConnected: Boolean = true,
	val openedCheckbox: Boolean = false,
	val siderealActive: Boolean = false,
	val ditheringEnabled: Boolean = false,
	val capturingActive: Boolean = false,
	val slewValue: Int = 0,
	val captureStartTime: Long? = null,
	val captureCount: Int? = null,
	val captureElapsedTimeMillis: Long? = null,
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
			return ditherValid && intervalometerValid
		} else {
			intervalometerValid
		}
	}
}