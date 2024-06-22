package og.ogstartracker.domain.usecases

import og.ogstartracker.domain.usecases.base.ResourceSuspendUseCase
import og.ogstartracker.repository.ArduinoRepository

class TurnTrackerRightUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendUseCase<Int, Unit> {

	override suspend fun invoke(input: Int) = repository.turnRight(input)
}