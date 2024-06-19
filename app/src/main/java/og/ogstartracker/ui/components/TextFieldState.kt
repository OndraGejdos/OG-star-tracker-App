package og.ogstartracker.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Suppress("EmptyMethod")
open class TextFieldState constructor(
	annotatedString: AnnotatedString,
	selection: TextRange = TextRange.Zero,
	composition: TextRange? = null,
	private val validator: Validator? = null,
) {

	var textState: TextFieldValue by mutableStateOf(
		TextFieldValue(
			annotatedString = annotatedString,
			selection = selection,
			composition = composition,
		),
	)
		private set

	private var uiState by mutableStateOf(TextFieldUiState())

	constructor(
		text: String,
		validator: Validator? = null,
		selection: TextRange = TextRange.Zero,
		composition: TextRange? = null,
	) : this(AnnotatedString(text), selection, composition, validator)

	open fun isValid() = validator?.isValid(textState.text) ?: true

	fun onFocusChange(focused: Boolean) {
		uiState = uiState.copy(isFocused = focused)
		if (!uiState.wasEverFocused && focused) {
			uiState = uiState.copy(wasEverFocused = true)
		}
	}

	fun setNewState(newState: TextFieldValue) {
		textState = newState
	}

	@Suppress("unused")
	fun setSelection(selection: TextRange) {
		textState = textState.copy(selection = selection)
	}
}

data class TextFieldUiState constructor(
	val wasEverFocused: Boolean = false,
	val isFocused: Boolean = false,
	val shouldShowErrors: Boolean = false,
)
