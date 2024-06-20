package og.ogstartracker.network

object ResourceUtils {
	fun <T : Any> notStarted(data: T? = null): Resource<T> =
		Resource(
			status = Status.NotStarted,
			data = data
		)

	fun <T : Any> success(data: T? = null): Resource<T> =
		Resource(
			status = Status.Success,
			data = data
		)

	fun <T : Any> error(errorIdentification: ErrorIdentification, data: T? = null): Resource<T> =
		Resource(
			Status.Error,
			data = data,
			errorIdentification = errorIdentification
		)

	fun <T : Any> error(
		data: T? = null,
		code: Int,
	): Resource<T> =
		Resource(
			status = Status.Error,
			data = data,
			errorIdentification = ErrorIdentificationImpl.parseErrorCodes(code)
		)
}
