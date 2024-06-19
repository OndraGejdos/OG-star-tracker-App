package og.ogstartracker.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import og.ogstartracker.R
import og.ogstartracker.drawColoredShadow
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.ColorShadow
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal200
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10Bold
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle16Bold

val text = """
	• LEVEL YOUR TRIPOD
	• PUT CAMERA INTO STAR TRACKER
	• FIND SOME BRIGHT STAR AND SET FOCUS
	• INSERT LASER OR POLAR SCOPE
	• TARGET POLARIS (SET YOUR ALT AND AZIMUT)
	• MOVE CAMERA TO YOUR SHOOT TARGET
	• TURN ON SIDEREAL TRACKING
	• GO FOR IT
""".trimIndent()

@Composable
fun ChecklistCard(
	enabled: Boolean,
	opened: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val interactionSource = remember { MutableInteractionSource() }

	val targetAngle = if (opened) 180f else 0f

	val angle by animateFloatAsState(
		targetValue = targetAngle,
		label = "",
		animationSpec = tween()
	)

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
			.clickable(
				interactionSource = interactionSource,
				indication = rememberRipple(
					color = ColorPrimary,
					bounded = true,
				),
				enabled = enabled
			) {
				onClick()
			}
			.animateContentSize()
			.alpha(1f.takeIf { enabled } ?: 0.5f)
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(GeneralIconSize),
				painter = painterResource(id = R.drawable.ic_format_list_checks),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = "CHECKLIST".uppercase(),
					style = textStyle16Bold,
					color = AppTheme.colorScheme.primary
				)
				Text(
					text = "Check if your prepared everything",
					style = textStyle10ItalicBold,
					color = AppTheme.colorScheme.secondary
				)
			}

			Image(
				painter = painterResource(
					id = R.drawable.ic_chevron_down
				),
				colorFilter = ColorFilter.tint(AppTheme.colorScheme.primary),
				contentDescription = null,
				modifier = Modifier
					.padding(end = DimensNormal100)
					.size(GeneralIconSize)
					.rotate(angle)
			)
		}

		if (opened) {
			Text(
				text = text,
				style = textStyle10Bold.copy(lineHeight = 22.sp),
				color = AppTheme.colorScheme.secondary,
				modifier = Modifier.padding(
					start = GeneralIconSize + DimensNormal200,
					bottom = DimensNormal100
				)
			)
		}
	}
}

@Preview
@Composable
fun ChecklistCardPreview() {
	AppTheme {
		Column {
			ChecklistCard(
				opened = false,
				onClick = {},
				enabled = true
			)
			ChecklistCard(
				opened = true,
				onClick = {},
				enabled = true
			)
		}
	}
}