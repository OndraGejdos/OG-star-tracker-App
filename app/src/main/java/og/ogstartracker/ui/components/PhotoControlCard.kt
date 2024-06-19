package og.ogstartracker.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.R
import og.ogstartracker.drawColoredShadow
import og.ogstartracker.ui.components.common.ActionInput
import og.ogstartracker.ui.screens.HomeUiState
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.ColorSecondary
import og.ogstartracker.ui.theme.ColorShadow
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensSmall100
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle14Bold
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.ui.theme.textStyle16Regular

@Composable
fun PhotoControlCard(
	uiState: HomeUiState,
	onPhotoControlEvent: (PhotoControlEvent) -> Unit,
	modifier: Modifier = Modifier
) {
	Column(
		modifier = modifier
			.padding(DimensNormal100)
			.fillMaxWidth()
			.drawColoredShadow(
				color = ColorShadow,
				alpha = 1f,
				borderRadius = 12.dp,
				spread = 4.dp,
				blurRadius = 12.dp,
			)
			.clip(ShapeNormal)
			.background(color = ColorBackground)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(GeneralIconSize),
				painter = painterResource(id = R.drawable.ic_camera_flip_outline),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = "Photo control".uppercase(),
					style = textStyle16Bold,
					color = AppTheme.colorScheme.primary
				)
				Text(
					text = "Send automated commands to your camera",
					style = textStyle10ItalicBold,
					color = AppTheme.colorScheme.secondary
				)
			}
		}

		Text(
			text = "INTERVALOMETER SETTINGS".uppercase(),
			style = textStyle14Bold,
			color = AppTheme.colorScheme.secondary,
			modifier = Modifier.padding(horizontal = DimensNormal100)
		)

		Column(
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(top = DimensSmall100)
		) {
			ActionInput(
				textFieldState = uiState.exposeTime,
				label = "Exposure length",
				placeholder = "0",
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Number,
				trailingIcon = {
					Text(
						"sec",
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				}
			)
			ActionInput(
				modifier = Modifier.padding(top = DimensSmall100),
				textFieldState = uiState.frameCount,
				label = "Number of exposures",
				placeholder = "0",
				imeAction = ImeAction.Done,
				keyboardType = KeyboardType.Number
			)
		}

		HorizontalDivider(
			color = AppTheme.colorScheme.shadow,
			thickness = 2.dp,
			modifier = Modifier.padding(vertical = DimensNormal100)
		)

		Text(
			text = "DITHERING SETTINGS".uppercase(),
			style = textStyle14Bold,
			color = AppTheme.colorScheme.secondary,
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(top = DimensSmall100)
		)
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(top = DimensNormal100)
		) {
			Column(
				modifier = Modifier
					.weight(1f)
					.padding(start = DimensNormal100)
			) {
				Text(
					text = "enable dithering".uppercase(),
					style = textStyle16Bold,
					color = ColorPrimary
				)
				Text(
					text = "Introduce small differences in targeting\nto suppress noise",
					style = textStyle10ItalicBold,
					color = ColorSecondary
				)
			}

			CustomSwitch(
				checked = uiState.ditheringEnabled,
				onCheckChange = {
					onPhotoControlEvent(PhotoControlEvent.DitheringActivation(!uiState.ditheringEnabled))
				},
				modifier = Modifier.padding(end = DimensNormal100)
			)
		}

		Column(
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(top = DimensNormal100)
		) {
			ActionInput(
				textFieldState = uiState.ditherFocalLength,
				label = "Focal length",
				placeholder = "0",
				imeAction = ImeAction.Next,
				keyboardType = KeyboardType.Number,
				trailingIcon = {
					Text(
						"mm",
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				}
			)
			ActionInput(
				modifier = Modifier.padding(top = DimensSmall100),
				textFieldState = uiState.ditherPixelSize,
				label = "Pixel size",
				placeholder = "0",
				imeAction = ImeAction.Done,
				keyboardType = KeyboardType.Number,
				trailingIcon = {
					Text(
						"nm",
						style = textStyle16Regular,
						color = AppTheme.colorScheme.secondary
					)
				}
			)
		}

		HorizontalDivider(
			color = AppTheme.colorScheme.shadow,
			thickness = 2.dp,
			modifier = Modifier.padding(vertical = DimensNormal100)
		)

		Row(
			horizontalArrangement = Arrangement.spacedBy(DimensNormal100),
			modifier = Modifier
				.padding(horizontal = DimensNormal100)
				.padding(bottom = DimensNormal100)
		) {
			Button(
				onClick = { onPhotoControlEvent(PhotoControlEvent.StartCapture) },
				colors = ButtonDefaults.buttonColors(
					containerColor = AppTheme.colorScheme.primary,
					contentColor = AppTheme.colorScheme.background,
					disabledContainerColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
					disabledContentColor = AppTheme.colorScheme.background
				),
				modifier = Modifier
					.weight(1f)
					.height(40.dp),
				contentPadding = PaddingValues(horizontal = DimensSmall100),
				enabled = !uiState.capturingActive
			) {
				Text(
					text = "Start capture".uppercase(),
					style = textStyle14Bold,
					color = AppTheme.colorScheme.background
				)
			}

			Button(
				onClick = { onPhotoControlEvent(PhotoControlEvent.EndCapture) },
				colors = ButtonDefaults.buttonColors(
					containerColor = AppTheme.colorScheme.primary,
					contentColor = AppTheme.colorScheme.background,
					disabledContainerColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
					disabledContentColor = AppTheme.colorScheme.background
				),
				modifier = Modifier
					.weight(1f)
					.height(40.dp),
				contentPadding = PaddingValues(horizontal = DimensSmall100),
				enabled = uiState.capturingActive
			) {
				Text(
					text = "End capture".uppercase(),
					style = textStyle14Bold,
					color = AppTheme.colorScheme.background
				)
			}
		}
	}
}

@Preview
@Composable
fun PhotoControlCardPreview() {
	AppTheme {
		PhotoControlCard(
			uiState = HomeUiState(),
			onPhotoControlEvent = {}
		)
	}
}