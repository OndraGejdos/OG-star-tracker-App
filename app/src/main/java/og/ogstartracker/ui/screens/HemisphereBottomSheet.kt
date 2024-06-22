package og.ogstartracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import og.ogstartracker.R
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.ui.components.common.Divider
import og.ogstartracker.ui.components.common.LocalInsets
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal150
import og.ogstartracker.ui.theme.DimensNormal200
import og.ogstartracker.ui.theme.textStyle20Bold
import og.ogstartracker.utils.closeBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HemisphereBottomSheet(
	onHemisphereClicked: (Hemisphere) -> Unit,
	onHide: () -> Unit,
) {
	val skipPartiallyExpanded by remember { mutableStateOf(false) }
	val scope = rememberCoroutineScope()
	val bottomSheetState = rememberModalBottomSheetState(
		skipPartiallyExpanded = skipPartiallyExpanded,
	)

	ModalBottomSheet(
		onDismissRequest = {
			closeBottomSheet(scope, bottomSheetState, onHide)
		},
		sheetState = bottomSheetState,
		windowInsets = WindowInsets(0),
		dragHandle = null
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					bottom = LocalInsets.current.navigationBarInset + DimensNormal200
				)
		) {
			Hemisphere.entries.forEach {
				HemisphereItem(
					hemisphere = it,
					onClick = {
						closeBottomSheet(scope, bottomSheetState, onHide) {
							onHemisphereClicked(it)
						}
					}
				)
				Divider()
			}
		}
	}
}

@Composable
fun HemisphereItem(
	hemisphere: Hemisphere,
	onClick: () -> Unit,
	modifier: Modifier = Modifier
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(
				horizontal = DimensNormal100,
				vertical = DimensNormal150
			)
			.clickable { onClick() }) {
		Icon(
			imageVector = ImageVector.vectorResource(id = R.drawable.ic_circle_half),
			contentDescription = null,
			tint = AppTheme.colorScheme.primary,
			modifier = Modifier.graphicsLayer {
				rotationZ = 90f.takeIf { hemisphere == Hemisphere.NORTH } ?: 270f
				translationY = 16f.takeIf { hemisphere == Hemisphere.NORTH } ?: -16f
			}
		)
		Text(
			modifier = Modifier.padding(start = DimensNormal100),
			text = stringResource(id = hemisphere.text),
			style = textStyle20Bold,
			color = AppTheme.colorScheme.primary
		)
	}
}

@Preview
@Composable
fun HemisphereItemPreview() {
	AppTheme {
		HemisphereItem(hemisphere = Hemisphere.NORTH, {})
	}
}
