package og.ogstartracker.repository

import og.ogstartracker.network.Resource

interface ArduinoRepository {

	suspend fun startSideRealTracking(): Resource<Unit>

	suspend fun stopSideRealTracking(): Resource<Unit>

	suspend fun turnLeft(): Resource<Unit>

	suspend fun turnRight(): Resource<Unit>

	suspend fun startCapture(): Resource<Unit>

	suspend fun abortCapture(): Resource<Unit>

	suspend fun getStatus(): Resource<String>
}