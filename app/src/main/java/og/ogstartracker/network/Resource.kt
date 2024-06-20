package og.ogstartracker.network

/**
 * A generic class that holds a value with its status.
 */
sealed class Resource<out T> {

	/**
	 * Resource state that shows that loading did not start or finish just yet.
	 */
	data object NotStarted : Resource<Nothing>()

	/**
	 * Resource state which shows that loading is in progress.
	 */
	data object Loading : Resource<Nothing>()

	/**
	 * Success state of Resource. This makes sure that data are always set and we do not reach state where status is
	 * success but no data are set.
	 *
	 * @property data success data
	 */
	data class Success<out T> constructor(val data: T) : Resource<T>()

	/**
	 * Error state of Resource. This makes sure that error is split from success and has it's own data.
	 *
	 * @property errorIdentification contains information about error
	 */
	data object Error : Resource<Nothing>()

	/**
	 * Checks if this resource is error. Might be useful for quick checks without the need to check the type.
	 *
	 * @return true if this is [Resource.Error]
	 */
	fun isError() = this is Error

	/**
	 * Checks if this resource is success. Might be useful for quick checks without the need to check the type.
	 *
	 * @return true if this is [Resource.Success]
	 */
	fun isSuccess() = this is Success

	/**
	 * Checks if this resource is loading. Might be useful for loading animations.
	 *
	 * @return true if this is [Resource.Loading]
	 */
	fun isLoading() = this is Loading

	/**
	 * Gets data or null without the need to check for success outside of [Resource].
	 *
	 * @return [T] on success else null
	 */
	fun getDataOrNull(): T? = if (this is Success) {
		this.data
	} else {
		null
	}
}