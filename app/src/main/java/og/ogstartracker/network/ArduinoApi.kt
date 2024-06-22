package og.ogstartracker.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArduinoApi {

	@GET("on")
	suspend fun startSiderealTracking(
		@Query("direction") direction: Int
	): Response<Unit>

	@GET("off")
	suspend fun stopSiderealTracking(): Response<Unit>

	@GET("left")
	suspend fun turnLeft(
		@Query("speed") speed: Int
	): Response<Unit>

	@GET("right")
	suspend fun turnRight(
		@Query("speed") speed: Int
	): Response<Unit>

	@GET("start")
	suspend fun startCapture(
		@Query("exposure") exposure: Int,
		@Query("numExposures") numExposures: Int,
		@Query("focalLength") focalLength: Int,
		@Query("pixSize") pixSize: Int,
		@Query("ditherEnabled") ditherEnabled: Int,
	): Response<Unit>

	@GET("abort")
	suspend fun abortCapture(): Response<Unit>

	@GET("status")
	suspend fun getStatus(): Response<String>
}