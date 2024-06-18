package og.ogstartracker.ui.components

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.R
import og.ogstartracker.drawColoredShadow
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.ColorSecondary
import og.ogstartracker.ui.theme.ColorShadow
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle16Bold

@Composable
fun CardBase(
	icon: Int,
	title: String,
	subtitle: String,
	modifier: Modifier = Modifier,
	rightPart: (@Composable () -> Unit)? = null,
) {
	val interactionSource = remember { MutableInteractionSource() }

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
			) {

			}
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.padding(vertical = DimensNormal100)
		) {
			Image(
				modifier = Modifier
					.padding(horizontal = DimensNormal100)
					.size(24.dp),
				painter = painterResource(id = icon),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = title,
					style = textStyle16Bold,
					color = ColorPrimary
				)
				Text(
					text = subtitle,
					style = textStyle10ItalicBold,
					color = ColorSecondary
				)
			}

			rightPart?.let { it() }
		}
	}
}

@Preview
@Composable
fun CardBasePreview() {
	CardBase(
		icon = R.drawable.ic_reload,
		title = "Test title",
		subtitle = "Test subtitle"
	)
}