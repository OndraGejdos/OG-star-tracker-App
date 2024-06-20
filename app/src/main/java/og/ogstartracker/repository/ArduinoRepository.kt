package og.ogstartracker.repository

import og.ogstartracker.network.Resource

interface ArduinoRepository {

	suspend fun startSideRealTracking(): Resource<Unit>
}