package og.ogstartracker.repository

import kotlinx.coroutines.flow.StateFlow
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.network.Resource

interface ArduinoRepository {

	val lastArduinoMessage: StateFlow<String?>

	suspend fun startSideRealTracking(direction: Hemisphere): Resource<String>

	suspend fun stopSideRealTracking(): Resource<String>

	suspend fun turnLeft(speed: Int): Resource<String>

	suspend fun turnRight(speed: Int): Resource<String>

	suspend fun startCapture(
		exposure: Int,
		numExposures: Int,
		focalLength: Int,
		pixSize: Int,
		ditherEnabled: Int,
	): Resource<String>

	suspend fun abortCapture(): Resource<String>

	suspend fun getStatus(): Resource<String>

	suspend fun resetLastMessage()

	suspend fun getVersion(): Resource<String>
}