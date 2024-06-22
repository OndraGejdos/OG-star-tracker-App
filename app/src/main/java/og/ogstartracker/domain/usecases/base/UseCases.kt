@file:Suppress("unused")

package og.ogstartracker.domain.usecases.base

import kotlinx.coroutines.flow.Flow
import og.ogstartracker.network.Resource

/**
 * Use case with input parameter.
 *
 * @param I input type
 * @param O output type
 */
fun interface UseCase<I, O> {
	operator fun invoke(input: I): O
}

/**
 * Use case which only provides result without any input.
 *
 * @param O output type
 */
fun interface ProviderUseCase<O> {
	operator fun invoke(): O
}

/**
 * [UseCase] with suspend.
 *
 * @param I input type
 * @param O output type
 */
interface SuspendUseCase<I, O> {
	suspend operator fun invoke(input: I): O
}

/**
 * [ProviderUseCase] with suspend.
 *
 * @param O output type
 */
interface SuspendProviderUseCase<O> {
	suspend operator fun invoke(): O
}

typealias ResourceUseCase<I, O> = UseCase<I, Resource<O>>
typealias ResourceProviderUseCase<O> = ProviderUseCase<Resource<O>>
typealias ResourceSuspendUseCase<I, O> = SuspendUseCase<I, Resource<O>>
typealias ResourceSuspendProviderUseCase<O> = SuspendProviderUseCase<Resource<O>>

typealias FlowUseCase<I, O> = UseCase<I, Flow<O>>
typealias FlowProviderUseCase<O> = ProviderUseCase<Flow<O>>
typealias FlowSuspendUseCase<I, O> = SuspendUseCase<I, Flow<O>>
typealias FlowSuspendProviderUseCase<O> = SuspendProviderUseCase<Flow<O>>
