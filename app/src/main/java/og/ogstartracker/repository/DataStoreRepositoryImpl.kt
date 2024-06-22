package og.ogstartracker.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import og.ogstartracker.domain.models.Hemisphere

class DataStoreRepositoryImpl constructor(
	private val context: Context
) : DataStoreRepository {

	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_DATA_STORE_NAME)

	override val hemisphere: Flow<Hemisphere> = context.dataStore.data
		.map { preferences ->
			Hemisphere.entries.firstOrNull {
				it.arduinoValue == preferences[HEMISPHERE]
			} ?: Hemisphere.NORTH
		}

	override suspend fun updateHemisphere(hemisphere: Hemisphere) {
		context.dataStore.edit { preferences ->
			preferences[HEMISPHERE] = hemisphere.arduinoValue
		}
	}

	companion object {
		private const val PREFS_DATA_STORE_NAME = "store"
		private val HEMISPHERE = intPreferencesKey("hemisphere")
	}
}