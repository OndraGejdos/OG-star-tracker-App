package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.usecases.base.ResourceSuspendProviderUseCase
import og.ogstartracker.repository.ArduinoRepository

class GetCurrentStateUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendProviderUseCase<String> {

	override suspend fun invoke() = repository.getStatus()
}