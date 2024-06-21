package og.ogstartracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import og.ogstartracker.ui.theme.DimensNormal75

@Suppress("unused")
@Composable
fun CleevioDialog(
	title: String,
	positiveButtonLabel: String,
	negativeButtonLabel: String,
	subtitle: String,
	onPositiveButton: () -> Unit,
	onNegativeButton: () -> Unit,
	positiveButtonTestTag: String,
	negativeButtonTestTag: String,
	modifier: Modifier = Modifier,
	openDialogState: MutableState<Boolean> = remember { mutableStateOf(false) },
	titleTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
	subtitleTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
	isCancelable: Boolean = true,
	onDismissRequest: () -> Unit = {
		if (isCancelable) {
			openDialogState.value = false
		}
	},
) {
	AlertDialog(
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(DimensNormal75)),
		onDismissRequest = onDismissRequest,
		title = {
			Text(
				modifier = Modifier
					.fillMaxWidth(),
				textAlign = TextAlign.Center,
				text = title,
				style = titleTextStyle,
			)
		},
		text = {
			Text(
				modifier = Modifier
					.fillMaxWidth(),
				textAlign = TextAlign.Center,
				text = subtitle,
				style = subtitleTextStyle,
			)
		},
		dismissButton = {
			Button(
				onClick = {
					onNegativeButton()
					if (isCancelable) {
						openDialogState.value = false
					}
				},
			) {
				Text("Close")
			}
		},
		confirmButton = {}
	)
}