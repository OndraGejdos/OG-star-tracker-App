package og.ogstartracker.domain.usecases.base

import og.ogstartracker.network.Resource

abstract class UseCaseNetwork<out T : Any, in Params> : UseCase<Resource<T>, Params>()
