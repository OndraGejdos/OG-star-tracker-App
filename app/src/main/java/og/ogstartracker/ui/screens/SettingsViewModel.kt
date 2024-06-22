package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.GetCurrentHemisphereFlowUseCase
import og.ogstartracker.domain.usecases.SetCurrentHemisphereUseCase
import og.ogstartracker.utils.WhileUiSubscribed

class SettingsViewModel internal constructor(
	private val setCurrentHemisphere: SetCurrentHemisphereUseCase,
	getCurrentHemisphereFlow: GetCurrentHemisphereFlowUseCase,
) : ViewModel() {

	private val _uiState = MutableStateFlow(SettingsUiState())
	val uiState = combine(
		getCurrentHemisphereFlow(),
		_uiState
	) { hemisphere, uiState ->
		uiState.copy(hemisphere = hemisphere)
	}.stateIn(viewModelScope, WhileUiSubscribed, SettingsUiState())

	internal fun saveHemisphere(hemisphere: Hemisphere) {
		viewModelScope.launch(Dispatchers.Default) {
			setCurrentHemisphere(hemisphere)
		}
	}
}

data class SettingsUiState internal constructor(
	val hemisphere: Hemisphere? = null,
)
