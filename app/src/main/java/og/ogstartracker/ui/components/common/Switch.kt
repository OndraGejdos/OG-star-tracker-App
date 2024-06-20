package og.ogstartracker.ui.components.common

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
	enabled: Boolean,
	onCheckChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	Switch(
		enabled = enabled,
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
			disabledUncheckedBorderColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
			disabledCheckedThumbColor = AppTheme.colorScheme.background,
			disabledCheckedTrackColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
			disabledCheckedIconColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
			disabledUncheckedThumbColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f)
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
			CustomSwitch(checked = false, enabled = true, {})
			CustomSwitch(checked = true, enabled = true, {})

			CustomSwitch(checked = false, enabled = false, {})
			CustomSwitch(checked = true, enabled = false, {})
		}
	}
}
