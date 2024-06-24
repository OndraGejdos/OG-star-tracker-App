package og.ogstartracker.domain.usecases.arduino

import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.base.ResourceSuspendUseCase
import og.ogstartracker.repository.ArduinoRepository

class StartSiderealTrackingUseCase constructor(
	private val repository: ArduinoRepository
) : ResourceSuspendUseCase<Hemisphere, Unit> {

	override suspend fun invoke(input: Hemisphere) = repository.startSideRealTracking(input)
}