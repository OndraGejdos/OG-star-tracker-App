package og.ogstartracker.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.network.ArduinoApi
import og.ogstartracker.utils.onSuccess
import og.ogstartracker.utils.tryOnline

class ArduinoRepositoryImpl constructor(
	private val arduinoApi: ArduinoApi,
) : ArduinoRepository {

	private val _lastArduinoMessage = MutableStateFlow<String?>(null)
	override val lastArduinoMessage = _lastArduinoMessage.asStateFlow()

	override suspend fun startSideRealTracking(direction: Hemisphere) = tryOnline {
		arduinoApi.startSiderealTracking(direction.arduinoValue)
	}.onSuccess { message ->
		_lastArduinoMessage.value = message
	}

	override suspend fun stopSideRealTracking() = tryOnline {
		arduinoApi.stopSiderealTracking()
	}.onSuccess { message ->
		_lastArduinoMessage.value = message
	}

	override suspend fun turnLeft(speed: Int) = tryOnline {
		arduinoApi.turnLeft(speed)
	}.onSuccess { message ->
		_lastArduinoMessage.value = message
	}

	override suspend fun turnRight(speed: Int) = tryOnline {
		arduinoApi.turnRight(speed)
	}.onSuccess { message ->
		_lastArduinoMessage.value = message
	}

	override suspend fun startCapture(
		exposure: Int,
		numExposures: Int,
		focalLength: Int,
		pixSize: Int,
		ditherEnabled: Int
	) = tryOnline {
		arduinoApi.startCapture(exposure, numExposures, focalLength, pixSize * 100, ditherEnabled)
	}.onSuccess { message ->
		_lastArduinoMessage.value = message
	}

	override suspend fun abortCapture() = tryOnline {
		arduinoApi.abortCapture()
	}.onSuccess { message ->
		_lastArduinoMessage.value = message
	}

	override suspend fun getStatus() = tryOnline {
		arduinoApi.getStatus()
	}.onSuccess { message ->
		_lastArduinoMessage.value = message
	}

	override suspend fun resetLastMessage() {
		_lastArduinoMessage.value = null
	}

	override suspend fun getVersion() = tryOnline {
		arduinoApi.getVersion()
	}
}