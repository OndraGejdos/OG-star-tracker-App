package og.ogstartracker.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import og.ogstartracker.SystemUiHelper
import og.ogstartracker.ui.components.ChecklistCard
import og.ogstartracker.ui.components.ConnectionCard
import og.ogstartracker.ui.components.LocalInsets
import og.ogstartracker.ui.components.PhotoControlCard
import og.ogstartracker.ui.components.PhotoControlEvent
import og.ogstartracker.ui.components.SiderealCard
import og.ogstartracker.ui.components.SlewControlCard
import og.ogstartracker.ui.components.SlewControlEvent
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensNormal200
import og.ogstartracker.ui.theme.DimensSmall100
import og.ogstartracker.ui.theme.textStyle20Bold
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
	navController: NavController,
	viewModel: DashboardViewModel = koinViewModel(),
) {
	SystemUiHelper(statusIconsLight = true, navigationIconsLight = true)

	val uiState by viewModel.uiState.collectAsState()

	DashboardScreenContent(
		uiState = uiState,
		onChecklistClicked = viewModel::changeChecklist,
		onSiderealClicked = viewModel::changeSidereal,
		onSlewControlEvent = viewModel::slewControlEvent,
		onPhotoControlEvent = viewModel::photoControlEvent,
	)
}

@Composable
private fun DashboardScreenContent(
	uiState: HomeUiState,
	onChecklistClicked: () -> Unit,
	onSiderealClicked: (Boolean) -> Unit,
	onSlewControlEvent: (SlewControlEvent) -> Unit,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		content = { paddings ->
			DashboardScreenLayout(
				modifier = Modifier.padding(top = paddings.calculateTopPadding()),
				uiState = uiState,
				onChecklistClicked = onChecklistClicked,
				onSiderealClicked = onSiderealClicked,
				onSlewControlEvent = onSlewControlEvent,
				onPhotoControlEvent = onPhotoControlEvent,
			)
		},
		containerColor = MaterialTheme.colorScheme.surface,
	)
}

@Composable
private fun DashboardScreenLayout(
	uiState: HomeUiState,
	onChecklistClicked: () -> Unit,
	onSlewControlEvent: (SlewControlEvent) -> Unit,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	onSiderealClicked: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	LazyColumn(
		modifier = modifier,
		contentPadding = PaddingValues(
			bottom = LocalInsets.current.navigationBarInset + DimensNormal200
		)
	) {
		item {
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.padding(vertical = DimensSmall100),
				text = "OG STAR TRACKER\nCONTROLLER",
				style = textStyle20Bold,
				textAlign = TextAlign.Center,
				color = AppTheme.colorScheme.primary,
			)
		}

		item {
			ConnectionCard(connected = uiState.connected)
		}

		item {
			HorizontalDivider(
				thickness = 2.dp,
				color = AppTheme.colorScheme.shadow
			)
		}

		item {
			ChecklistCard(
				opened = uiState.openedCheckbox,
				onClick = onChecklistClicked
			)
		}

		item {
			SiderealCard(
				active = uiState.siderealActive,
				onCheckChanged = {
					onSiderealClicked(!uiState.siderealActive)
				},
			)
		}

		item {
			SlewControlCard(
				slewControlCommands = onSlewControlEvent,
				stepSize = uiState.slewValue,
			)
		}

		item {
			PhotoControlCard(
				uiState = uiState,
				onPhotoControlEvent = onPhotoControlEvent
			)
		}
	}
}

@Composable
@Preview
internal fun HomeScreenContentPreview() {
	AppTheme {
		DashboardScreenContent(
			uiState = HomeUiState(),
			onChecklistClicked = {},
			onSiderealClicked = {},
			onSlewControlEvent = {},
			onPhotoControlEvent = {}
		)
	}
}
