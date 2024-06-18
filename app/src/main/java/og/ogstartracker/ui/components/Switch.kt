package og.ogstartracker.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensSmall50

@Composable
fun CustomSwitch(
	checked: Boolean,
	onCheckChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	Switch(
		modifier = modifier,
		checked = checked,
		onCheckedChange = onCheckChange,
		colors = SwitchDefaults.colors(
			checkedThumbColor = AppTheme.colorScheme.background,
			checkedTrackColor = AppTheme.colorScheme.primary,
			checkedBorderColor = AppTheme.colorScheme.primary,
			checkedIconColor = AppTheme.colorScheme.primary,
			uncheckedTrackColor = AppTheme.colorScheme.background,
			uncheckedBorderColor = AppTheme.colorScheme.primary,
			uncheckedThumbColor = AppTheme.colorScheme.primary,
		),
		thumbContent = if (checked) {
			{
				Icon(
					imageVector = Icons.Sharp.Check,
					contentDescription = null,
					modifier = Modifier.padding(DimensSmall50)
				)
			}
		} else {
			null
		},
	)
}

@Preview
@Composable
fun BananaSwitchPreview() {
	AppTheme {
		Column {
			CustomSwitch(checked = false, {})
			CustomSwitch(checked = true, {})
		}
	}
}
