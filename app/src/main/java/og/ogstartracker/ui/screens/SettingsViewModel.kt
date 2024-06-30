package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.arduino.GetVersionUseCase
import og.ogstartracker.domain.usecases.settings.GetCurrentHemisphereFlowUseCase
import og.ogstartracker.domain.usecases.settings.SetCurrentHemisphereUseCase
import og.ogstartracker.utils.WhileUiSubscribed
import og.ogstartracker.utils.onSuccess

class SettingsViewModel internal constructor(
	private val setCurrentHemisphere: SetCurrentHemisphereUseCase,
	private val getVersion: GetVersionUseCase,
	getCurrentHemisphereFlow: GetCurrentHemisphereFlowUseCase,
) : ViewModel() {

	private val _uiState = MutableStateFlow(SettingsUiState())
	val uiState = combine(
		getCurrentHemisphereFlow(),
		_uiState
	) { hemisphere, uiState ->
		uiState.copy(hemisphere = hemisphere)
	}.stateIn(viewModelScope, WhileUiSubscribed, SettingsUiState())

	init {
		viewModelScope.launch(Dispatchers.Default) {
			getVersion().onSuccess { version ->
				_uiState.update { it.copy(version = version ?: 0) }
			}
		}
	}

	internal fun saveHemisphere(hemisphere: Hemisphere) {
		viewModelScope.launch(Dispatchers.Default) {
			setCurrentHemisphere(hemisphere)
		}
	}
}

data class SettingsUiState internal constructor(
	val hemisphere: Hemisphere? = null,
	val version: Int? = null,
)
