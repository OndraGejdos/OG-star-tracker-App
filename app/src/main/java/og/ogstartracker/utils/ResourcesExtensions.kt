package og.ogstartracker.utils

import og.ogstartracker.network.ErrorIdentification
import og.ogstartracker.network.Resource
import og.ogstartracker.network.Status

inline fun <T, S> Resource<T>.map(mapAction: (T?) -> S): Resource<S> = when (this.status) {
	is Status.Success -> Resource(Status.Success, mapAction(this.data))
	is Status.Error -> Resource(Status.Error, null, this.errorIdentification)
	is Status.Loading -> Resource(Status.Loading)
	is Status.NotStarted -> Resource(Status.NotStarted)
}

inline infix fun <T, S> Resource<T>.chain(chainAction: (T) -> Resource<S>): Resource<S> = when (this.status) {
	is Status.Success -> chainAction(this.data!!)
	is Status.Error -> Resource(Status.Error, null, this.errorIdentification)
	is Status.Loading -> Resource(Status.Loading)
	is Status.NotStarted -> Resource(Status.NotStarted)
}

/**
 * Runs a [block] function only when current [Resource] is loading. Returns this to allow chaining of additional
 * functions.
 *
 * @param block of code to run on loading
 * @return [Resource] with [T] which the same as call [Resource]
 */
inline fun <T> Resource<T>.onLoading(block: () -> Unit): Resource<T> {
	if (this.status is Status.Loading) {
		block()
	}
	return this
}

/**
 * Runs a [block] function only when current [Resource] is not started. Returns this to allow chaining of additional
 * functions.
 *
 * @param block of code to run on loading
 * @return [Resource] with [T] which the same as call [Resource]
 */
inline fun <T> Resource<T>.onNotStarted(block: () -> Unit): Resource<T> {
	if (this.status is Status.NotStarted) {
		block()
	}
	return this
}

/**
 * Runs a [block] function only when current [Resource] is successful. Returns this to allow chaining of additional
 * functions.
 *
 * Taken from: [here](https://github.com/eManPrague/kaal/blob/master/kaal-domain/src/main/kotlin/cz/eman/kaal/domain
 * /result/ResultExtensions.kt).
 * Why? Same author :D
 *
 * @param block of code to run on success
 * @return [Resource] with [T] which the same as call [Resource]
 */
inline fun <T> Resource<T>.onSuccess(block: (T?) -> Unit): Resource<T> {
	if (this.status is Status.Success) {
		block(this.data)
	}
	return this
}

/**
 * Runs a [block] function only when current [Resource] fails with an error. Returns this to allow chaining of
 * additional functions.
 *
 * Taken from: [here](https://github.com/eManPrague/kaal/blob/master/kaal-domain/src/main/kotlin/cz/eman/kaal/domain
 * /result/ResultExtensions.kt).
 * Why? Same author :D
 *
 * @param block of code to run on error
 * @return [Resource] which the same as call [Resource]
 */
inline fun <T> Resource<T>.onError(block: (ErrorIdentification?) -> Unit): Resource<T> {
	if (this.status is Status.Error) {
		block(this.errorIdentification)
	}
	return this
}

/**
 * Runs a [block] function after the Resource finishes. Does not return anything to make sure that this function is
 * always called last in the function chain.
 *
 * Taken from: [here](https://github.com/eManPrague/kaal/blob/master/kaal-domain/src/main/kotlin/cz/eman/kaal/domain
 * /result/ResultExtensions.kt).
 * Why? Same author :D
 *
 * @param block of code to run
 */
fun <T> Resource<T>.onFinished(block: (Resource<T>) -> Unit) {
	block(this)
}