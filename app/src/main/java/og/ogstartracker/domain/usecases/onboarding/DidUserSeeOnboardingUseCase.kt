package og.ogstartracker.domain.usecases.onboarding

import og.ogstartracker.domain.usecases.base.FlowProviderUseCase
import og.ogstartracker.repository.DataStoreRepository

class DidUserSeeOnboardingUseCase constructor(
	private val repository: DataStoreRepository
) : FlowProviderUseCase<Boolean> {

	override fun invoke() = repository.userSawOnboarding
}