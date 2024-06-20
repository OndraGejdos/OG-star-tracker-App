package og.ogstartracker.repository

import og.ogstartracker.network.ArduinoApi
import og.ogstartracker.network.Resource
import og.ogstartracker.utils.tryOnline

class ArduinoRepositoryImpl constructor(
	private val arduinoApi: ArduinoApi,
) : ArduinoRepository {

	override suspend fun startSideRealTracking(): Resource<Unit> = tryOnline {
		arduinoApi.startSiderealTracking()
	}
}