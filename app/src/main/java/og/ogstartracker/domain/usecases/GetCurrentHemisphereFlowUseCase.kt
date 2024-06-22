package og.ogstartracker.domain.usecases

import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.base.FlowProviderUseCase
import og.ogstartracker.repository.DataStoreRepository

class GetCurrentHemisphereFlowUseCase constructor(
	private val dataStoreRepository: DataStoreRepository
) : FlowProviderUseCase<Hemisphere> {

	override fun invoke() = dataStoreRepository.hemisphere
}