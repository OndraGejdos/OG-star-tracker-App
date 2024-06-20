package og.ogstartracker.events

sealed class PhotoControlEvent {

	data class DitheringActivation constructor(
		val active: Boolean
	) : PhotoControlEvent()

	data object StartCapture : PhotoControlEvent()

	data object EndCapture : PhotoControlEvent()
}