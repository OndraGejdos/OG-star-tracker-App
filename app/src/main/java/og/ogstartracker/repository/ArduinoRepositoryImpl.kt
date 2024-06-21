package og.ogstartracker.repository

import og.ogstartracker.network.ArduinoApi
import og.ogstartracker.utils.tryOnline

class ArduinoRepositoryImpl constructor(
	private val arduinoApi: ArduinoApi,
) : ArduinoRepository {

	override suspend fun startSideRealTracking() = tryOnline {
		arduinoApi.startSiderealTracking()
	}

	override suspend fun stopSideRealTracking() = tryOnline {
		arduinoApi.stopSiderealTracking()
	}

	override suspend fun turnLeft() = tryOnline {
		arduinoApi.turnLeft()
	}

	override suspend fun turnRight() = tryOnline {
		arduinoApi.turnRight()
	}

	override suspend fun startCapture() = tryOnline {
		arduinoApi.startCapture()
	}

	override suspend fun abortCapture() = tryOnline {
		arduinoApi.abortCapture()
	}

	override suspend fun getStatus() = tryOnline {
		arduinoApi.getStatus()
	}
}