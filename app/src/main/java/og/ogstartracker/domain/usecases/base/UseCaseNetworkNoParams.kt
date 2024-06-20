package og.ogstartracker.domain.usecases.base

import og.ogstartracker.network.Resource

abstract class UseCaseNetworkNoParams<out T : Any> : UseCase<Resource<T>, Unit>() {

	suspend operator fun invoke() = super.invoke(Unit)
}
