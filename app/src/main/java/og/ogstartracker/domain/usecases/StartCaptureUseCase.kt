package og.ogstartracker.domain.usecases

import og.ogstartracker.domain.usecases.base.UseCaseNetworkNoParams
import og.ogstartracker.repository.ArduinoRepository

class StartCaptureUseCase constructor(
	private val repository: ArduinoRepository
) : UseCaseNetworkNoParams<Unit>() {

	override suspend fun doWork(params: Unit) = repository.startCapture()
}