package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import og.ogstartracker.Config.SLEW_MAX_VALUE
import og.ogstartracker.Config.SLEW_MIN_VALUE
import og.ogstartracker.domain.events.PhotoControlEvent
import og.ogstartracker.domain.events.SlewControlEvent
import og.ogstartracker.domain.usecases.AbortCaptureUseCase
import og.ogstartracker.domain.usecases.StartCaptureUseCase
import og.ogstartracker.domain.usecases.StartSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.StopSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.TurnTrackerLeftUseCase
import og.ogstartracker.domain.usecases.TurnTrackerRightUseCase
import og.ogstartracker.ui.components.common.input.NotEmptyValidator
import og.ogstartracker.ui.components.common.input.TextFieldState
import og.ogstartracker.utils.VibratorController
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
) : ViewModel() {

	private val _uiState = MutableStateFlow(HomeUiState())
	val uiState = _uiState.asStateFlow()

	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	internal fun changeSidereal(active: Boolean) {
		if (active) {
			sendCommand { startSiderealTracking() }
			vibratorController.startVibrations(vibrationPatternThreeClick)
		} else {
			sendCommand { stopSiderealTracking() }
			vibratorController.startVibrations(vibrationPatternClick)
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
				sendCommand { trackerLeft() }
				vibratorController.startVibrations(vibrationPatternClick)
			}

			SlewControlEvent.RotateClockwise -> {
				sendCommand { trackerRight }
				vibratorController.startVibrations(vibrationPatternClick)
			}
		}
	}

	internal fun photoControlEvent(photoControlEvent: PhotoControlEvent) {
		when (photoControlEvent) {
			is PhotoControlEvent.DitheringActivation -> {
				_uiState.update {
					it.copy(ditheringEnabled = photoControlEvent.active)
				}
				if (photoControlEvent.active) {
					vibratorController.startVibrations(vibrationPatternThreeClick)
				} else {
					vibratorController.startVibrations(vibrationPatternClick)
				}
			}

			PhotoControlEvent.EndCapture -> {
				sendCommand { abortCapture() }
				vibratorController.startVibrations(vibrationPatternClick)
				_uiState.update { it.copy(capturingActive = false) }
			}

			PhotoControlEvent.StartCapture -> {
				sendCommand { startCapture() }
				vibratorController.startVibrations(vibrationPatternThreeClick)
				_uiState.update { it.copy(capturingActive = true) }
			}
		}
	}

	private fun sendCommand(command: suspend () -> Unit) {
		viewModelScope.launch(Dispatchers.Default) {
			command()
		}
	}
}

data class HomeUiState internal constructor(
	val trackerConnected: Boolean = true,
	val openedCheckbox: Boolean = false,
	val siderealActive: Boolean = false,
	val ditheringEnabled: Boolean = false,
	val capturingActive: Boolean = false,
	val slewValue: Int = 0,
	val exposeTime: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val frameCount: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val ditherFocalLength: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val ditherPixelSize: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
)
