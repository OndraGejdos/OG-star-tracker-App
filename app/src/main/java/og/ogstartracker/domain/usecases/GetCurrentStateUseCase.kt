package og.ogstartracker.domain.usecases

import og.ogstartracker.domain.usecases.base.UseCaseNetworkNoParams
import og.ogstartracker.repository.ArduinoRepository

class GetCurrentStateUseCase constructor(
	private val repository: ArduinoRepository
) : UseCaseNetworkNoParams<String>() {

	override suspend fun doWork(params: Unit) = repository.getStatus()
}