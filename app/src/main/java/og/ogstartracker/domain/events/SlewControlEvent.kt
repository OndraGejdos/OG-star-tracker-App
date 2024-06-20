package og.ogstartracker.domain.events

sealed class SlewControlEvent {

	data object Plus : SlewControlEvent()

	data object Minus : SlewControlEvent()

	data object RotateClockwise : SlewControlEvent()

	data object RotateAnticlockwise : SlewControlEvent()
}