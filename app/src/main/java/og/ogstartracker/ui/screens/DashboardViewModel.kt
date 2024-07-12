package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import og.ogstartracker.Config.CHECK_WIFI_DURATION
import og.ogstartracker.Config.SLEW_MAX_VALUE
import og.ogstartracker.Config.SLEW_MIN_VALUE
import og.ogstartracker.Config.STATUS_TRACKING_ON
import og.ogstartracker.domain.events.PhotoControlEvent
import og.ogstartracker.domain.events.SlewControlEvent
import og.ogstartracker.domain.models.CheckListItem
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
import kotlin.math.roundToInt

private const val SECOND = 1000L

/**
 * ViewModel for the Dashboard screen of the OG Star Tracker app.
 *
 * This ViewModel handles the UI logic for the Dashboard screen, including sidereal tracking,
 * photo capture control, slew mechanism control, and WiFi connection checks. It interacts with
 * various use cases to perform actions like starting and stopping sidereal tracking, managing
 * photo captures, and updating UI state based on user interactions and background operations.
 *
 * @property vibratorController An instance of [VibratorController] for handling vibrations.
 * @property useCases An instance of [DashboardUseCaseProvider] providing access to the use cases needed by the Dashboard.
 */
class DashboardViewModel internal constructor(
	private val vibratorController: VibratorController,
	private val useCases: DashboardUseCaseProvider,
) : ViewModel() {

	private var runTimer: Boolean = false
	private var photoCaptureTimerJob: Job? = null
	private var wifiTimerJob: Job? = null

	private val _checkWifiEvent = MutableStateFlow(false)
	val checkWifiEvent = _checkWifiEvent.asStateFlow()

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

	init {
		// fetch info if tracker is already in sidereal state
		viewModelScope.launch(Dispatchers.Default) {
			useCases.getCurrentState().onSuccess { status ->
				_uiState.update { it.copy(siderealActive = status == STATUS_TRACKING_ON) }
			}
		}
	}

	/**
	 * Main ui state of the screen. Holds everything ui related.
	 */
	private val _uiState = MutableStateFlow(DashboardUiState())
	val uiState = combine(
		_uiState,
		useCases.getCurrentHemisphereFlow(),
		useCases.didUserSeeOnboarding(),
		useCases.getLastArduinoMessage()
	) { uiState, hemisphere, userSawOnboarding, lastMessage ->
		uiState.copy(
			hemisphere = hemisphere,
			shouldShowOnboardingDialog = !userSawOnboarding,
			lastMessage = lastMessage
		)
	}.stateIn(
		scope = viewModelScope,
		started = WhileUiSubscribed,
		initialValue = DashboardUiState()
	)

	/**
	 * This changes visibility of the checkbox on the screen.
	 */
	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	/**
	 * This enables or disables sidereal tracking.
	 */
	internal fun changeSidereal(active: Boolean) {
		if (active) {
			sendCommand {
				useCases.startSiderealTracking(uiState.value.hemisphere ?: return@sendCommand).onSuccess {
					vibratorController.startVibrations(vibrationPatternThreeClick)
					_uiState.update { it.copy(siderealActive = true) }
				}
			}
		} else {
			sendCommand {
				useCases.stopSiderealTracking().onSuccess {
					vibratorController.startVibrations(vibrationPatternClick)
					_uiState.update { it.copy(siderealActive = false) }
				}
			}
		}
	}

	/**
	 * This controls slew mechanism.
	 */
	internal fun slewControlEvent(slewControlEvent: SlewControlEvent) {
		when (slewControlEvent) {
			SlewControlEvent.Minus -> {
				val newValue = (uiState.value.slewValue - 1).coerceAtLeast(SLEW_MIN_VALUE)
				_uiState.update { it.copy(slewValue = newValue) }
				vibratorController.startVibrations(vibrationPatternClick)
				notifyCacheAboutChange(SettingItem.SLEW_SPEED, newValue)
			}

			SlewControlEvent.Plus -> {
				val newValue = (uiState.value.slewValue + 1).coerceAtMost(SLEW_MAX_VALUE)
				_uiState.update { it.copy(slewValue = newValue) }
				vibratorController.startVibrations(vibrationPatternClick)
				notifyCacheAboutChange(SettingItem.SLEW_SPEED, newValue)
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

	/**
	 * This control photo capture mechanism.
	 */
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
						stopPhotoCaptureTimer()
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
							} else 0,
							pixSize = if (uiState.value.ditheringEnabled) {
								(uiState.value.ditherPixelSize.textState.text.toDouble() * 100.0).roundToInt()
							} else 0,
							ditherEnabled = if (uiState.value.ditheringEnabled) 1 else 0,
						)
					).onSuccess {
						_uiState.update { it.copy(capturingActive = true, captureStartTime = System.currentTimeMillis()) }
						startPhotoCaptureTimer()
						vibratorController.startVibrations(vibrationPatternThreeClick)
					}
				}
			}
		}
	}

	/**
	 * Helper function for communicating with arduino.
	 */
	private fun sendCommand(command: suspend () -> Unit) {
		viewModelScope.launch(Dispatchers.Default) {
			command()
		}
	}

	/**
	 * Starts photo capture timer.
	 */
	private fun startPhotoCaptureTimer() {
		photoCaptureTimerJob?.cancel()

		val exposureTime = uiState.value.exposeTime.textState.text.toIntOrNull() ?: return
		val frameCount = uiState.value.frameCount.textState.text.toIntOrNull() ?: return
		val ditherActive = uiState.value.ditheringEnabled

		val fullDitherTime = (0.0.takeIf { !ditherActive } ?: ((frameCount / 3 * 2))).toInt()
		val estimatedTime = (exposureTime * frameCount + (((frameCount - 1) * 3) + fullDitherTime)) * SECOND

		photoCaptureTimerJob = viewModelScope.launch(Dispatchers.Default) {
			_uiState.update {
				it.copy(captureEstimatedTimeMillis = estimatedTime)
			}
			val captureStarTime = System.currentTimeMillis()
			while (estimatedTime > ((System.currentTimeMillis() - captureStarTime))) {
				delay(SECOND)
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

			stopPhotoCaptureTimer()
			photoControlEvent(PhotoControlEvent.EndCapture)
		}
	}

	/**
	 * Stops photo capture timer.
	 */
	private fun stopPhotoCaptureTimer() {
		photoCaptureTimerJob?.cancel()
	}

	/**
	 * Called when user changed some settings.
	 */
	internal fun notifyCacheAboutChange(settingItem: SettingItem, value: Int?) {
		viewModelScope.launch(Dispatchers.Default) {
			useCases.setNewSettings(SetNewSettingsUseCase.Input(settingItem, value))
		}
	}

	/**
	 * Saves event when user saw onboarding so that its not presented again on app start.
	 */
	internal fun setUserSawOnboard() {
		viewModelScope.launch(Dispatchers.Default) {
			useCases.setUserSawOnboarding()
		}
	}

	/**
	 * Track value if user did allow location permission.
	 */
	internal fun setHaveLocationPermission(active: Boolean) {
		_uiState.update { it.copy(haveLocationPermission = active) }
	}

	/**
	 * Track value if user have wifi connection.
	 */
	internal fun setConnection(wifiConnected: Boolean) {
		_uiState.update { it.copy(wifiConnected = wifiConnected) }
	}

	/**
	 * Resets last arduino message so that messages could be repeated (like "slewing").
	 * StateFlow holds last value and will not trigger event if the value is the same.
	 */
	internal fun resetMessage() {
		viewModelScope.launch(Dispatchers.Default) {
			useCases.resetLastArduinoMessage()
		}
	}

	/**
	 * Resets StateFlow so that UI can be informed again.
	 */
	internal fun resetWifiEvent() {
		_checkWifiEvent.value = false
	}

	/**
	 * Stops repeatedly checking is user have the correct wifi.
	 */
	internal fun stopWiFiTimer() {
		runTimer = false
		wifiTimerJob?.cancel()
	}

	/**
	 * Starts wifi check timer.
	 */
	internal fun startWiFiTimer() {
		runTimer = true
		startWiFiTimerJob()
	}

	/**
	 * Manage checklist items.
	 */
	internal fun updateCheckListItem(checkListItem: CheckListItem) {
		_uiState.update {
			it.copy(
				checkListItems = it.checkListItems.map { item ->
					if (item.text == checkListItem.text) {
						item.copy(checked = !item.checked)
					} else {
						item
					}
				}
			)
		}
	}

	/**
	 * Starts coroutine that wifi timer is running on.
	 */
	private fun startWiFiTimerJob() {
		wifiTimerJob = viewModelScope.launch(Dispatchers.Default) {
			while (runTimer) {
				delay(CHECK_WIFI_DURATION)
				_checkWifiEvent.value = true
			}
		}
	}
}

data class DashboardUiState internal constructor(
	val hemisphere: Hemisphere? = null,
	val wifiConnected: Boolean = false,
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
	val checkListItems: List<CheckListItem> = defaultCheckListItems
) {
//	fun getCaptureRatio() = buildString {
//		append(captureCount ?: 0)
//		append("/")
//		append(frameCount.textState.text.toIntOrNull() ?: 0)
//	}

	/**
	 * Returns true if every required input have correct value.
	 * Required inputs change according to what user enabled.
	 */
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

private val defaultCheckListItems = listOf(
	CheckListItem("FIND A NICE AND STABLE PLACE FOR TRIPOD", false),
	CheckListItem("LEVEL YOUR TRIPOD", false),
	CheckListItem("ATTACH STAR TRACKER TO YOUR TRIPOD", false),
	CheckListItem("ATTACH CAMERA TO STAR TRACKER", false),
	CheckListItem("FIND SOME BRIGHT STAR AND FIND IT IN YOUR CAMERA LIVE VIEW", false),
	CheckListItem("SET FOCUS (YOU CAN USE BAHTINOV MASK)", false),
	CheckListItem("INSERT LASER (OR SKIP)", false),
	CheckListItem("TARGET POLARIS (SET ROUGHLY YOUR ALTITUDE AND AZIMUTH)", false),
	CheckListItem("REDIRECT CAMERA TO YOUR SHOOT TARGET", false),
	CheckListItem("INSERT POLAR SCOPE (OR SKIP)", false),
	CheckListItem("CALIBRATE NORTH CELESTIAL POLE (OR SKIP)", false),
	CheckListItem("CHECK CAMERA TARGET AGAIN (OR SKIP)", false),
	CheckListItem("CHECK POLAR SCOPE AGAIN (OR SKIP)", false),
	CheckListItem("TURN ON SIDEREAL TRACKING", false),
	CheckListItem("TAKE LIGHT FRAMES (YOU CAN USE INTERVALOMETER SETTINGS), TAKE AS MANY AS YOU WANT", false),
	CheckListItem("TAKE DARK FRAMES (SAME SETTINGS, JUST COVER LENS WITH CAP, THE SAME OUTSIDE TEMPERATURE IS NEEDED!), TAKE 20", false),
	CheckListItem("TAKE FLAT FRAMES (USE WHITE SHEET OF PAPER WITH WHITE PHONE SCREEN AND COVER THE LENS), TAKE 20", false),
	CheckListItem("TAKE BIAS FRAMES (USE LENS COVER, FASTEST SHUTTER SPEED), TAKE 50-100", false),
)
