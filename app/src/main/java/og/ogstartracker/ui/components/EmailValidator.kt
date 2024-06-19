package og.ogstartracker.ui.components

class NotEmptyValidator : Validator {
	override fun isValid(text: String) = text.isNotBlank()
}
