package og.ogstartracker.domain.usecases.settings

import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.base.SuspendUseCase
import og.ogstartracker.repository.DataStoreRepository

class SetCurrentHemisphereUseCase constructor(
	private val dataStoreRepository: DataStoreRepository
) : SuspendUseCase<Hemisphere, Unit> {

	override suspend fun invoke(input: Hemisphere) = dataStoreRepository.updateHemisphere(input)
}