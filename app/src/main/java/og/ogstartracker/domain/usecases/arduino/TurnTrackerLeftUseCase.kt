package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.usecases.base.ResourceSuspendUseCase
import og.ogstartracker.repository.ArduinoRepository

class TurnTrackerLeftUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendUseCase<Int, String> {

	override suspend fun invoke(input: Int) = repository.turnLeft(input)
}