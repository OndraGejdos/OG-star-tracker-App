package og.ogstartracker.repository

import kotlinx.coroutines.flow.Flow
import og.ogstartracker.domain.models.Hemisphere
import og.ogstartracker.domain.usecases.SettingItem

interface DataStoreRepository {

	val hemisphere: Flow<Hemisphere>

	val exposureTime: Flow<Int?>

	val exposureCount: Flow<Int?>

	val focalLength: Flow<Int?>

	val pixelSize: Flow<Int?>

	val slewSpeed: Flow<Int?>

	val ditherActive: Flow<Int>

	suspend fun updateHemisphere(hemisphere: Hemisphere)

	suspend fun setNewSettings(settingItem: SettingItem, value: Int?)
}