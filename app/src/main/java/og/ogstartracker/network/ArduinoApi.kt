package og.ogstartracker.network

import retrofit2.Response
import retrofit2.http.GET

interface ArduinoApi {

	@GET("on")
	suspend fun startSiderealTracking(): Response<Unit>

	@GET("off")
	suspend fun stopSiderealTracking(): Response<Unit>

	@GET("left")
	suspend fun turnLeft(): Response<Unit>

	@GET("right")
	suspend fun turnRight(): Response<Unit>

	@GET("start")
	suspend fun startCapture(): Response<Unit>

	@GET("abort")
	suspend fun abortCapture(): Response<Unit>

	@GET("status")
	suspend fun getStatus(): Response<String>
}
