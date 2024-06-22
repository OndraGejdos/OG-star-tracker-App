package og.ogstartracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import og.ogstartracker.R
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.ui.components.common.Divider
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal150
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.ui.theme.textStyle16Regular
import og.ogstartracker.ui.theme.textStyle20Bold
import og.ogstartracker.utils.SystemUiHelper
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
	navController: NavController,
	viewModel: SettingsViewModel = koinViewModel(),
) {
	SystemUiHelper(statusIconsLight = true, navigationIconsLight = true)

	var showHemisphereBS by remember {
		mutableStateOf(false)
	}

	val uiState by viewModel.uiState.collectAsState()

	SettingsScreenContent(
		uiState = uiState,
		onBack = navController::navigateUp,
		onHemisphereClick = {
			showHemisphereBS = true
		}
	)

	if (showHemisphereBS) {
		HemisphereBottomSheet(onHemisphereClicked = viewModel::saveHemisphere) {
			showHemisphereBS = false
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenContent(
	uiState: SettingsUiState,
	onBack: () -> Unit,
	onHemisphereClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			TopAppBar(
				title = {
					Text(
						text = stringResource(id = R.string.settings_title),
						style = textStyle20Bold,
						color = AppTheme.colorScheme.primary
					)
				},
				navigationIcon = {
					IconButton(
						onClick = onBack
					) {
						Icon(
							imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_left),
							tint = AppTheme.colorScheme.primary,
							contentDescription = null
						)
					}
				}
			)
		},
		content = { paddings ->
			SettingsScreenLayout(
				modifier = Modifier.padding(top = paddings.calculateTopPadding()),
				uiState = uiState,
				onHemisphereClick = onHemisphereClick
			)
		},
		containerColor = MaterialTheme.colorScheme.surface,
	)
}

@Composable
private fun SettingsScreenLayout(
	uiState: SettingsUiState,
	onHemisphereClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier.fillMaxSize()) {
		Divider()
		Hemisphere(
			onClick = onHemisphereClick,
			hemisphere = uiState.hemisphere ?: Hemisphere.NORTH
		)
		Divider()
	}
}

@Composable
fun Hemisphere(
	hemisphere: Hemisphere,
	onClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = DimensNormal100, vertical = DimensNormal150)
			.clickable {
				onClick()
			}
	) {
		Text(
			text = stringResource(id = R.string.settings_hemisphere),
			style = textStyle16Regular,
			color = AppTheme.colorScheme.primary,
			modifier = modifier.weight(1f)
		)
		Text(
			text = stringResource(id = hemisphere.text).uppercase(),
			style = textStyle16Bold,
			color = AppTheme.colorScheme.primary
		)
	}
}

@Preview
@Composable
fun HemispherePreview() {
	AppTheme {
		Hemisphere(
			onClick = {},
			hemisphere = Hemisphere.NORTH
		)
	}
}

@Composable
@Preview
internal fun SettingContentPreview() {
	AppTheme {
		SettingsScreenContent(
			uiState = SettingsUiState(),
			onBack = {},
			onHemisphereClick = {}
		)
	}
}
