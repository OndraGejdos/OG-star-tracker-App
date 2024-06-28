package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.usecases.base.SuspendProviderUseCase
import og.ogstartracker.repository.ArduinoRepository

class ResetLastArduinoMessageUseCase constructor(
	private val repository: ArduinoRepository
) : SuspendProviderUseCase<Unit> {

	override suspend fun invoke() = repository.resetLastMessage()
}