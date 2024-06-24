package og.ogstartracker.domain.usecases

import og.ogstartracker.domain.usecases.base.SuspendProviderUseCase
import og.ogstartracker.repository.DataStoreRepository

class SetUserSawOnboardingUseCase constructor(
	private val repository: DataStoreRepository
) : SuspendProviderUseCase<Unit> {

	override suspend fun invoke() = repository.setUserSawOnboarding()
}