package og.ogstartracker.repository

import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.network.Resource

interface ArduinoRepository {

	suspend fun startSideRealTracking(direction: Hemisphere): Resource<Unit>

	suspend fun stopSideRealTracking(): Resource<Unit>

	suspend fun turnLeft(speed: Int): Resource<Unit>

	suspend fun turnRight(speed: Int): Resource<Unit>

	suspend fun startCapture(
		exposure: Int,
		numExposures: Int,
		focalLength: Int,
		pixSize: Int,
		ditherEnabled: Int,
	): Resource<Unit>

	suspend fun abortCapture(): Resource<Unit>

	suspend fun getStatus(): Resource<String>
}