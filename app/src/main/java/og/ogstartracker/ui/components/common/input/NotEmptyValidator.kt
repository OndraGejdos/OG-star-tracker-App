package og.ogstartracker.ui.components.common.input

class NotEmptyValidator : Validator {
	override fun isValid(text: String) = text.isNotBlank()
}
