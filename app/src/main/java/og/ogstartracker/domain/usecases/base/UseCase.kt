package og.ogstartracker.domain.usecases.base

abstract class UseCase<out T, in Params> {

	suspend operator fun invoke(params: Params): T = doWork(params)

	protected abstract suspend fun doWork(params: Params): T
}
