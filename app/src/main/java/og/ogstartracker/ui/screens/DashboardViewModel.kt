package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import og.ogstartracker.ui.components.NotEmptyValidator
import og.ogstartracker.ui.components.PhotoControlEvent
import og.ogstartracker.ui.components.SlewControlEvent
import og.ogstartracker.ui.components.TextFieldState

class DashboardViewModel internal constructor() : ViewModel() {

	private val _uiState = MutableStateFlow(HomeUiState())
	val uiState = _uiState.asStateFlow()

	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	internal fun changeSidereal(active: Boolean) {
		_uiState.update { it.copy(siderealActive = active) }
	}

	fun slewControlEvent(slewControlEvent: SlewControlEvent) {
		when (slewControlEvent) {
			SlewControlEvent.Minus -> _uiState.update {
				it.copy(slewValue = (it.slewValue - 1).coerceAtLeast(0))
			}

			SlewControlEvent.Plus -> _uiState.update {
				it.copy(slewValue = (it.slewValue + 1).coerceAtMost(5))
			}

			SlewControlEvent.RotateAnticlockwise -> {
				// TODO call arduino
			}

			SlewControlEvent.RotateClockwise -> {
				// TODO call arduino
			}
		}
	}

	internal fun photoControlEvent(photoControlEvent: PhotoControlEvent) {
		when (photoControlEvent) {
			is PhotoControlEvent.DitheringActivation -> _uiState.update {
				it.copy(ditheringEnabled = photoControlEvent.active)
			}

			PhotoControlEvent.EndCapture -> {
				// TODO call arduino
			}

			PhotoControlEvent.StartCapture -> {
				// TODO call arduino
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
