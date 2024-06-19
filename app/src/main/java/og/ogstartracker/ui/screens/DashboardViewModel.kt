package og.ogstartracker.ui.screens

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import og.ogstartracker.Config.SLEW_MAX_VALUE
import og.ogstartracker.Config.SLEW_MIN_VALUE
import og.ogstartracker.ui.components.NotEmptyValidator
import og.ogstartracker.ui.components.PhotoControlEvent
import og.ogstartracker.ui.components.SlewControlEvent
import og.ogstartracker.ui.components.TextFieldState
import og.ogstartracker.utils.VibratorController
import og.ogstartracker.utils.vibrationPatternClick
import og.ogstartracker.utils.vibrationPatternThreeClick

class DashboardViewModel internal constructor(
	private val context: Context,
) : ViewModel() {

	private val vibratorController = VibratorController()

	private val _uiState = MutableStateFlow(HomeUiState())
	val uiState = _uiState.asStateFlow()

	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	internal fun changeSidereal(active: Boolean) {
		if (active) {
			// TODO call arduino
		} else {
			// TODO call arduino
		}

		_uiState.update { it.copy(siderealActive = active) }

		if (active) {
			vibratorController.startVibrations(context, vibrationPatternThreeClick)
		} else {
			vibratorController.startVibrations(context, vibrationPatternClick)
		}
	}

	fun slewControlEvent(slewControlEvent: SlewControlEvent) {
		when (slewControlEvent) {
			SlewControlEvent.Minus -> {
				_uiState.update {
					it.copy(slewValue = (it.slewValue - 1).coerceAtLeast(SLEW_MIN_VALUE))
				}
				vibratorController.startVibrations(context, vibrationPatternClick)
			}

			SlewControlEvent.Plus -> {
				_uiState.update {
					it.copy(slewValue = (it.slewValue + 1).coerceAtMost(SLEW_MAX_VALUE))
				}
				vibratorController.startVibrations(context, vibrationPatternClick)
			}

			SlewControlEvent.RotateAnticlockwise -> {
				// TODO call arduino
				vibratorController.startVibrations(context, vibrationPatternClick)
			}

			SlewControlEvent.RotateClockwise -> {
				// TODO call arduino
				vibratorController.startVibrations(context, vibrationPatternClick)
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
					vibratorController.startVibrations(context, vibrationPatternThreeClick)
				} else {
					vibratorController.startVibrations(context, vibrationPatternClick)
				}
			}

			PhotoControlEvent.EndCapture -> {
				// TODO call arduino
				_uiState.update { it.copy(capturingActive = false) }
				vibratorController.startVibrations(context, vibrationPatternClick)
			}

			PhotoControlEvent.StartCapture -> {
				// TODO call arduino
				_uiState.update { it.copy(capturingActive = true) }
				vibratorController.startVibrations(context, vibrationPatternThreeClick)
			}
		}
	}
}

data class HomeUiState internal constructor(
	val connected: Boolean = false,
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
