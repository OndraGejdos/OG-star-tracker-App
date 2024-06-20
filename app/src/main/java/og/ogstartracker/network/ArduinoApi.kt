package og.ogstartracker.network

import retrofit2.Response
import retrofit2.http.GET

interface ArduinoApi {

	@GET("start")
	suspend fun startSiderealTracking(): Response<Unit>
}