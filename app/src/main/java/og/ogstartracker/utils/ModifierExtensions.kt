package og.ogstartracker.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensNormal100

fun Modifier.segmentedShadow(
	color: Color,
	layers: Int = 10,
	shadowWidth: Dp = DimensNormal100
): Modifier {
	return this.drawBehind {
		val shadowSize = shadowWidth.toPx()
		val size = shadowSize / layers

		repeat(layers) {
			drawRoundRect(
				style = Stroke(width = size),
				color = color.copy(alpha = 1f - (1 / layers.toFloat()) * it),
				topLeft = Offset(
					shadowSize - it * size,
					shadowSize - it * size
				),
				cornerRadius = CornerRadius(shadowSize + it * size),
				size = Size(
					this.size.width - shadowSize * 2 + size * it * 2,
					this.size.height - shadowSize * 2 + size * it * 2
				)
			)
		}
	}
}

@Preview
@Composable
fun AsdPreview() {
	AppTheme {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(200.dp)
				.segmentedShadow(AppTheme.colorScheme.primary)
		)
	}
}