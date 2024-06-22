package og.ogstartracker.domain.usecases

import og.ogstartracker.domain.usecases.base.ResourceSuspendProviderUseCase
import og.ogstartracker.repository.ArduinoRepository

class AbortCaptureUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendProviderUseCase<Unit> {

	override suspend fun invoke() = repository.abortCapture()
}