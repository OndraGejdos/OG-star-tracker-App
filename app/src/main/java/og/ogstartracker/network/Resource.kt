package og.ogstartracker.network

data class Resource<out T : Any?> constructor(
	val status: Status,
	val data: T? = null,
	val errorIdentification: ErrorIdentification? = null
) {

	fun isLoading(): Boolean = status is Status.Loading

	fun isSuccess(): Boolean = status is Status.Success

	fun isError(): Boolean = status is Status.Error

	fun isNotStarted(): Boolean = status is Status.NotStarted
}
