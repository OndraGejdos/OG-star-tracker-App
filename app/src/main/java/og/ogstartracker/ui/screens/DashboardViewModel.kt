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
import og.ogstartracker.domain.usecases.StartSiderealTrackingUseCase
import og.ogstartracker.ui.components.common.input.NotEmptyValidator
import og.ogstartracker.ui.components.common.input.TextFieldState
import og.ogstartracker.utils.VibratorController
import og.ogstartracker.utils.vibrationPatternClick
import og.ogstartracker.utils.vibrationPatternThreeClick

class DashboardViewModel internal constructor(
	private val vibratorController: VibratorController,
	private val startSiderealTracking: StartSiderealTrackingUseCase,
) : ViewModel() {

	private val _uiState = MutableStateFlow(HomeUiState())
	val uiState = _uiState.asStateFlow()

	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	internal fun changeSidereal(active: Boolean) {
		if (active) {
			viewModelScope.launch(Dispatchers.Default) {
				startSiderealTracking()
			}
		} else {
			// TODO call arduino
		}

		_uiState.update { it.copy(siderealActive = active) }

		if (active) {
			vibratorController.startVibrations(vibrationPatternThreeClick)
		} else {
			vibratorController.startVibrations(vibrationPatternClick)
		}
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
				// TODO call arduino
				vibratorController.startVibrations(vibrationPatternClick)
			}

			SlewControlEvent.RotateClockwise -> {
				// TODO call arduino
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
				// TODO call arduino
				_uiState.update { it.copy(capturingActive = false) }
				vibratorController.startVibrations(vibrationPatternClick)
			}

			PhotoControlEvent.StartCapture -> {
				// TODO call arduino
				_uiState.update { it.copy(capturingActive = true) }
				vibratorController.startVibrations(vibrationPatternThreeClick)
			}
		}
	}
}

data class HomeUiState internal constructor(
	val trackerConnected: Boolean = true,
	val openedCheckbox: Boolean = false,
	val siderealActive: Boolean = false,
	val slewValue: Int = 0,
	val exposeTime: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val frameCount: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val ditheringEnabled: Boolean = false,
	val ditherFocalLength: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val ditherPixelSize: TextFieldState = TextFieldState(text = "", validator = NotEmptyValidator()),
	val capturingActive: Boolean = false,
)
