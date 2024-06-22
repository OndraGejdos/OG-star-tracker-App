package og.ogstartracker.repository

import kotlinx.coroutines.flow.Flow
import og.ogstartracker.domain.models.Hemisphere

interface DataStoreRepository {

	val hemisphere: Flow<Hemisphere>

	suspend fun updateHemisphere(hemisphere: Hemisphere)

}