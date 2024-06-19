package og.ogstartracker.ui.components.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import og.ogstartracker.ui.components.TextFieldState
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.ShapeNormal
import og.ogstartracker.ui.theme.textStyle12Bold
import og.ogstartracker.ui.theme.textStyle16Bold
import og.ogstartracker.ui.theme.textStyle16Regular

@Composable
fun ActionInput(
	label: String,
	modifier: Modifier = Modifier,
	placeholder: String? = null,
	textFieldState: TextFieldState = TextFieldState(text = ""),
	imeAction: ImeAction = ImeAction.Done,
	singleLine: Boolean = true,
	readOnly: Boolean = false,
	enabled: Boolean = true,
	labelTextStyle: TextStyle = textStyle16Regular,
	placeholderTextStyle: TextStyle = textStyle16Regular,
	textStyle: TextStyle = textStyle16Regular,
	leadingIcon: @Composable (() -> Unit)? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	keyboardType: KeyboardType = KeyboardType.Text,
	capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
	interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
	keyboardActions: KeyboardActions = KeyboardActions(),
	visualTransformation: VisualTransformation = VisualTransformation.None,
	onValueChange: (TextFieldValue) -> Unit = { textFieldState.setNewState(it) },
) {
	OutlinedTextField(
		modifier = modifier
			.fillMaxWidth()
			.defaultMinSize(100.dp)
			.onFocusChanged { focusState ->
				textFieldState.onFocusChange(focusState.isFocused)
			},
		value = textFieldState.textState,
		onValueChange = onValueChange,
		enabled = enabled,
		textStyle = textStyle,
		colors = OutlinedTextFieldDefaults.colors(
			focusedContainerColor = Color.Transparent,
			focusedBorderColor = AppTheme.colorScheme.primary,
			unfocusedBorderColor = AppTheme.colorScheme.primary,
			unfocusedContainerColor = Color.Transparent,
			focusedTextColor = AppTheme.colorScheme.secondary,
			unfocusedTextColor = AppTheme.colorScheme.secondary,
			focusedLabelColor = AppTheme.colorScheme.secondary,
			focusedPlaceholderColor = AppTheme.colorScheme.secondary,
			unfocusedLabelColor = AppTheme.colorScheme.secondary,
			disabledBorderColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
			disabledTextColor = AppTheme.colorScheme.secondary.copy(alpha = 0.5f),
			disabledLabelColor = AppTheme.colorScheme.secondary.copy(alpha = 0.5f)
		),
		visualTransformation = visualTransformation,
		singleLine = singleLine,
		readOnly = readOnly,
		interactionSource = interactionSource,
		shape = ShapeNormal,
		label = {
			Text(
				text = label,
				style = labelTextStyle,
			)
		},
		placeholder = {
			Text(
				text = placeholder ?: label,
				style = placeholderTextStyle,
			)
		},
		trailingIcon = trailingIcon,
		leadingIcon = leadingIcon,
		keyboardOptions = KeyboardOptions(
			imeAction = imeAction,
			keyboardType = keyboardType,
			capitalization = capitalization,
		),
		keyboardActions = keyboardActions,
	)
}

@Preview
@Composable
fun ActionInputPreview() {
	AppTheme {
		ActionInput(
			label = "Label",
		)
	}
}