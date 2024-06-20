package og.ogstartracker.ui.components.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.Constants
import og.ogstartracker.R
import og.ogstartracker.ui.components.common.CustomSwitch
import og.ogstartracker.utils.drawColoredShadow
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ColorBackground
import og.ogstartracker.ui.theme.ColorPrimary
import og.ogstartracker.ui.theme.ColorSecondary
import og.ogstartracker.ui.theme.ColorShadow
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.GeneralIconSize
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle10ItalicBold
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.utils.drawShadow

@Composable
fun SiderealCard(
	enabled: Boolean,
	active: Boolean,
	onCheckChanged: (Boolean) -> Unit,
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
				painter = painterResource(id = R.drawable.ic_tracking),
				contentDescription = null,
				colorFilter = ColorFilter.tint(ColorPrimary)
			)

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = stringResource(id = R.string.sidereal_tracking_title).uppercase(),
					style = textStyle16Bold,
					color = ColorPrimary
				)
				Text(
					text = stringResource(id = R.string.sidereal_tracking_subtitle),
					style = textStyle10ItalicBold,
					color = ColorSecondary
				)
			}

			CustomSwitch(
				checked = active,
				onCheckChange = onCheckChanged,
				modifier = Modifier.padding(end = DimensNormal100),
				enabled = enabled
			)
		}
	}
}

@Preview
@Composable
fun SiderealCardPreview() {
	AppTheme {
		Column {
			SiderealCard(
				onCheckChanged = {},
				active = true,
				enabled = true
			)
			SiderealCard(
				onCheckChanged = {},
				active = false,
				enabled = true
			)
		}
	}
}