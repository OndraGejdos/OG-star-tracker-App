package og.ogstartracker.ui.screens

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject internal constructor() : ViewModel() {

	private val _uiState = MutableStateFlow(HomeUiState())
	val uiState = _uiState.asStateFlow()
}

data class HomeUiState internal constructor(
	val number: Int? = null
)
