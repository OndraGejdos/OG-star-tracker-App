package og.ogstartracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import og.ogstartracker.SystemUiHelper
import og.ogstartracker.ui.theme.AppTheme

@Composable
fun MainScreen(
	navController: NavController,
	viewModel: HomeViewModel = hiltViewModel(),
) {
	SystemUiHelper(statusIconsLight = true, navigationIconsLight = true)

	val uiState by viewModel.uiState.collectAsState()

	HomeScreenContent(
		uiState = uiState,
	)
}

@Composable
private fun HomeScreenContent(
	uiState: HomeUiState,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		content = { insetsPadding ->
			HomeScreenLayout(
				uiState = uiState,
				insetsPadding = insetsPadding,
			)
		},
		containerColor = MaterialTheme.colorScheme.surface,
	)
}

@Composable
private fun HomeScreenLayout(
	uiState: HomeUiState,
	insetsPadding: PaddingValues,
) {
	Box { }
}

@Composable
@Preview
internal fun HomeScreenContentPreview() {
	AppTheme {
		HomeScreenContent(uiState = HomeUiState())
	}
}
