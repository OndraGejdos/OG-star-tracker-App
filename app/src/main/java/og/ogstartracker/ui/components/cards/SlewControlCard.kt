package og.ogstartracker.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.Constants
import og.ogstartracker.R
import og.ogstartracker.events.SlewControlEvent
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal150
import og.ogstartracker.ui.theme.DimensNormal200
import og.ogstartracker.ui.theme.DimensSmall100
import og.ogstartracker.ui.theme.DimensSmall125
import og.ogstartracker.ui.theme.DimensSmall25
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10Bold
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle12Bold
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.ui.theme.textStyle20Bold
import og.ogstartracker.utils.drawShadow

@Composable
fun SlewControlCard(
	enabled: Boolean,
	stepSize: Int,
	slewControlCommands: (SlewControlEvent) -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.padding(DimensNormal100)
			.fillMaxWidth()
			.drawShadow()
			.clip(ShapeNormal)
			.background(color = ColorBackground)
			.alpha(Constants.Percent._100.takeIf { enabled } ?: Constants.Percent._50)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(GeneralIconSize),
				painter = painterResource(id = R.drawable.ic_rotate_360),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.slew_control_title).uppercase(),
					style = textStyle16Bold,
					color = AppTheme.colorScheme.primary
				)
				Text(
					text = stringResource(id = R.string.slew_control_subtitle),
					style = textStyle10ItalicBold,
					color = AppTheme.colorScheme.secondary
				)
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				text = stringResource(id = R.string.slew_control_step_size).uppercase(),
				style = textStyle10Bold,
				color = AppTheme.colorScheme.secondary,
				modifier = Modifier
					.padding(start = GeneralIconSize + DimensNormal200)
					.weight(1f)
			)

			IconButton(
				onClick = {
					slewControlCommands(SlewControlEvent.Minus)
				},
				enabled = enabled
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_minus_circle),
					contentDescription = null,
					tint = AppTheme.colorScheme.primary
				)
			}

			Text(
				text = stepSize.toString(),
				style = textStyle20Bold,
				color = AppTheme.colorScheme.primary,
				modifier = Modifier.width(DimensNormal150),
				textAlign = TextAlign.Center
			)

			IconButton(
				onClick = {
					slewControlCommands(SlewControlEvent.Plus)
				},
				enabled = enabled
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_plus_circle),
					contentDescription = null,
					tint = AppTheme.colorScheme.primary
				)
			}
		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(
				end = DimensNormal100,
				bottom = DimensNormal100,
				top = DimensSmall100
			),
		) {
			Text(
				text = stringResource(id = R.string.slew_control_move).uppercase(),
				style = textStyle10Bold,
				color = AppTheme.colorScheme.secondary,
				modifier = Modifier
					.padding(start = GeneralIconSize + DimensNormal200)
					.weight(1f)
			)

			Button(
				enabled = enabled,
				onClick = {
					slewControlCommands(SlewControlEvent.RotateAnticlockwise)
				},
				contentPadding = PaddingValues(horizontal = DimensSmall125),
				colors = ButtonDefaults.buttonColors(
					containerColor = AppTheme.colorScheme.primary,
					contentColor = AppTheme.colorScheme.background,
					disabledContainerColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
					disabledContentColor = AppTheme.colorScheme.background
				),
				modifier = Modifier
					.padding(end = DimensSmall100)
					.height(40.dp)
			) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Text(
						text = stringResource(id = R.string.slew_control_acw),
						style = textStyle12Bold
					)
					Icon(
						painter = painterResource(id = R.drawable.ic_restore),
						contentDescription = null,
						modifier = Modifier
							.size(DimensNormal150)
							.padding(start = DimensSmall25)
					)
				}
			}

			Button(
				enabled = enabled,
				onClick = {
					slewControlCommands(SlewControlEvent.RotateClockwise)
				},
				contentPadding = PaddingValues(
					horizontal = DimensSmall125,
				),
				colors = ButtonDefaults.buttonColors(
					containerColor = AppTheme.colorScheme.primary,
					contentColor = AppTheme.colorScheme.background,
					disabledContainerColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
					disabledContentColor = AppTheme.colorScheme.background
				),
				modifier = Modifier.height(40.dp),
			) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Text(
						text = stringResource(id = R.string.slew_control_cw),
						style = textStyle12Bold
					)
					Icon(
						painter = painterResource(id = R.drawable.ic_restore),
						contentDescription = null,
						modifier = Modifier
							.size(DimensNormal150)
							.padding(start = DimensSmall25)
							.graphicsLayer {
								scaleX = -1f
							}
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun SlewControlCardPreview() {
	AppTheme {
		Column {
			SlewControlCard(
				slewControlCommands = {},
				stepSize = 4,
				enabled = true
			)
		}
	}
}