package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.usecases.base.FlowProviderUseCase
import og.ogstartracker.repository.ArduinoRepository

class GetLastArduinoMessageUseCase constructor(
	private val repository: ArduinoRepository
) : FlowProviderUseCase<String?> {

	override fun invoke() = repository.lastArduinoMessage
}