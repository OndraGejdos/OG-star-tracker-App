package og.ogstartracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import og.ogstartracker.R
import og.ogstartracker.domain.events.PhotoControlEvent
import og.ogstartracker.domain.events.SlewControlEvent
import og.ogstartracker.domain.usecases.SettingItem
import og.ogstartracker.ui.components.InfoDialog
import og.ogstartracker.ui.components.cards.ChecklistCard
import og.ogstartracker.ui.components.cards.ConnectionCard
import og.ogstartracker.ui.components.cards.PhotoControlCard
import og.ogstartracker.ui.components.cards.SiderealCard
import og.ogstartracker.ui.components.cards.SlewControlCard
import og.ogstartracker.ui.components.common.Divider
import og.ogstartracker.ui.components.common.LocalInsets
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal200
import og.ogstartracker.ui.theme.DimensNormal75
import og.ogstartracker.ui.theme.DimensSmall100
import og.ogstartracker.ui.theme.DimensSmall50
import og.ogstartracker.ui.theme.textStyle20Bold
import og.ogstartracker.utils.SystemUiHelper
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
	navController: NavController,
	viewModel: DashboardViewModel = koinViewModel(),
) {
	SystemUiHelper(statusIconsLight = true, navigationIconsLight = true)

	var showInfoDialog by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	val uiState by viewModel.uiState.collectAsState()

	if (uiState.shouldShowOnboardingDialog) {
		showInfoDialog = true
	}

	LaunchedEffect(Unit) {
		scope.launch {
			viewModel.settingsItemsFlow.first()
		}
	}

	DashboardScreenContent(
		uiState = uiState,
		onChecklistClicked = viewModel::changeChecklist,
		onSiderealClicked = viewModel::changeSidereal,
		onSlewControlEvent = viewModel::slewControlEvent,
		onPhotoControlEvent = viewModel::photoControlEvent,
		onGearClick = {
			navController.navigate("settings")
		},
		onInfoClick = {
			showInfoDialog = true
		},
		notifyAboutChange = viewModel::notifyAboutChange,
	)

	if (showInfoDialog) {
		InfoDialog(onHide = {
			viewModel.setUserSawOnboard()
			showInfoDialog = false
		})
	}
}

@Composable
private fun DashboardScreenContent(
	uiState: DashboardUiState,
	onChecklistClicked: () -> Unit,
	onSiderealClicked: (Boolean) -> Unit,
	onSlewControlEvent: (SlewControlEvent) -> Unit,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	onGearClick: () -> Unit,
	notifyAboutChange: (SettingItem, Int?) -> Unit,
	onInfoClick: () -> Unit,
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
				onGearClick = onGearClick,
				onInfoClick = onInfoClick,
				notifyAboutChange = notifyAboutChange
			)
		},
		containerColor = MaterialTheme.colorScheme.surface,
	)
}

@Composable
private fun DashboardScreenLayout(
	uiState: DashboardUiState,
	onChecklistClicked: () -> Unit,
	onSlewControlEvent: (SlewControlEvent) -> Unit,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	onGearClick: () -> Unit,
	notifyAboutChange: (SettingItem, Int?) -> Unit,
	onInfoClick: () -> Unit,
	onSiderealClicked: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val state = rememberLazyListState()

	LazyColumn(
		state = state,
		modifier = modifier,
		contentPadding = PaddingValues(
			top = DimensNormal100,
			bottom = LocalInsets.current.navigationBarInset + DimensNormal200
		),
		verticalArrangement = Arrangement.spacedBy(DimensSmall50)
	) {
		item {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				IconButton(onClick = { onInfoClick() }) {
					Icon(
						imageVector = ImageVector.vectorResource(R.drawable.ic_information),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null,
						modifier = Modifier
							.size(48.dp)
							.padding(DimensSmall100)
					)
				}

				Text(
					modifier = Modifier
						.weight(1f)
						.padding(vertical = DimensSmall100),
					text = stringResource(id = R.string.main_title).uppercase(),
					style = textStyle20Bold,
					textAlign = TextAlign.Center,
					color = AppTheme.colorScheme.primary,
				)

				IconButton(onClick = { onGearClick() }) {
					Icon(
						imageVector = ImageVector.vectorResource(R.drawable.ic_cog),
						tint = AppTheme.colorScheme.primary,
						contentDescription = null,
						modifier = Modifier
							.size(48.dp)
							.padding(DimensSmall100)
					)
				}
			}
		}

		item {
			ConnectionCard(connected = uiState.trackerConnected)
		}

		item {
			Divider(modifier = Modifier.padding(vertical = DimensNormal75))
		}

		item {
			ChecklistCard(
				opened = uiState.openedCheckbox,
				onClick = onChecklistClicked,
				enabled = uiState.trackerConnected,
			)
		}

		item {
			SiderealCard(
				active = uiState.siderealActive,
				onCheckChanged = {
					onSiderealClicked(!uiState.siderealActive)
				},
				enabled = uiState.trackerConnected
			)
		}

		item {
			SlewControlCard(
				slewControlCommands = onSlewControlEvent,
				stepSize = uiState.slewValue,
				enabled = uiState.trackerConnected,
			)
		}

		item {
			PhotoControlCard(
				uiState = uiState,
				onPhotoControlEvent = onPhotoControlEvent,
				notifyAboutChange = notifyAboutChange
			)
		}
	}
}

@Composable
@Preview
internal fun HomeScreenContentPreview() {
	AppTheme {
		DashboardScreenContent(
			uiState = DashboardUiState(),
			onChecklistClicked = {},
			onSiderealClicked = {},
			onSlewControlEvent = {},
			onPhotoControlEvent = {},
			onInfoClick = {},
			onGearClick = {},
			notifyAboutChange = { _, _ -> }
		)
	}
}
