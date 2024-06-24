package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.usecases.base.ResourceSuspendUseCase
import og.ogstartracker.repository.ArduinoRepository

class StartCaptureUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendUseCase<StartCaptureUseCase.Input, Unit> {

	override suspend fun invoke(input: Input) = repository.startCapture(
		exposure = input.exposure,
		numExposures = input.numExposures,
		focalLength = input.focalLength,
		pixSize = input.pixSize,
		ditherEnabled = input.ditherEnabled
	)

	data class Input constructor(
		val exposure: Int,
		val numExposures: Int,
		val focalLength: Int,
		val pixSize: Int,
		val ditherEnabled: Int,
	)
}