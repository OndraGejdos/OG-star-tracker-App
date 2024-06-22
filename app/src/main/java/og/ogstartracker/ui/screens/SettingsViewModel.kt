package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import og.ogstartracker.domain.models.Hemisphere

class SettingsViewModel internal constructor(
) : ViewModel() {

	private val _uiState = MutableStateFlow(SettingsUiState())
	val uiState = _uiState.asStateFlow()

	private fun sendCommand(command: suspend () -> Unit) {
		viewModelScope.launch(Dispatchers.Default) {
			command()
		}
	}

	internal fun saveHemisphere(hemisphere: Hemisphere) {

	}
}

data class SettingsUiState internal constructor(
	val slewValue: Int = 0,
)
