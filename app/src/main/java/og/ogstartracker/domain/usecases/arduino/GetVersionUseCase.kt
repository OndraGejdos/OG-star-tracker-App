package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.usecases.base.ResourceSuspendProviderUseCase
import og.ogstartracker.repository.ArduinoRepository
import og.ogstartracker.utils.map

class GetVersionUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendProviderUseCase<Int?> {

	override suspend fun invoke() = repository.getVersion().map {
		it?.toIntOrNull()
	}
}