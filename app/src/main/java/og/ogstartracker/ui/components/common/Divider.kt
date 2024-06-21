package og.ogstartracker.ui.components.common

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.ui.theme.AppTheme

@Composable
fun Divider(modifier: Modifier = Modifier) {
	HorizontalDivider(
		modifier = modifier,
		thickness = 2.dp,
		color = AppTheme.colorScheme.shadow
	)
}

@Preview
@Composable
fun DividerPreview() {
	Divider()
}