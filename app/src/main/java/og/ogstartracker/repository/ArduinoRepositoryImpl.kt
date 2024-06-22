package og.ogstartracker.repository

import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.network.ArduinoApi
import og.ogstartracker.utils.tryOnline

class ArduinoRepositoryImpl constructor(
	private val arduinoApi: ArduinoApi,
) : ArduinoRepository {

	override suspend fun startSideRealTracking(direction: Hemisphere) = tryOnline {
		arduinoApi.startSiderealTracking(direction.arduinoValue)
	}

	override suspend fun stopSideRealTracking() = tryOnline {
		arduinoApi.stopSiderealTracking()
	}

	override suspend fun turnLeft(speed: Int) = tryOnline {
		arduinoApi.turnLeft(speed)
	}

	override suspend fun turnRight(speed: Int) = tryOnline {
		arduinoApi.turnRight(speed)
	}

	override suspend fun startCapture(
		exposure: Int,
		numExposures: Int,
		focalLength: Int,
		pixSize: Int,
		ditherEnabled: Int
	) = tryOnline {
		arduinoApi.startCapture(exposure, numExposures, focalLength, pixSize * 100, ditherEnabled)
	}

	override suspend fun abortCapture() = tryOnline {
		arduinoApi.abortCapture()
	}

	override suspend fun getStatus() = tryOnline {
		arduinoApi.getStatus()
	}
}