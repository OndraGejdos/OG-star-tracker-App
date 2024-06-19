package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel internal constructor() : ViewModel() {

	private val _uiState = MutableStateFlow(HomeUiState())
	val uiState = _uiState.asStateFlow()

	internal fun changeChecklist() {
		_uiState.update { it.copy(openedCheckbox = !it.openedCheckbox) }
	}

	internal fun changeSidereal(active: Boolean) {
		_uiState.update { it.copy(siderealActive = active) }
	}
}

data class HomeUiState internal constructor(
	val connected: Boolean = false,
	val openedCheckbox: Boolean = false,
	val siderealActive: Boolean = false,
)
