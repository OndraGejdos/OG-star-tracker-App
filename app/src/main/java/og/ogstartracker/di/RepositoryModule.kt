package og.ogstartracker.di

import og.ogstartracker.repository.ArduinoRepository
import og.ogstartracker.repository.ArduinoRepositoryImpl
import og.ogstartracker.repository.DataStoreRepository
import og.ogstartracker.repository.DataStoreRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

	single<ArduinoRepository> {
		ArduinoRepositoryImpl(
			arduinoApi = get()
		)
	}

	single<DataStoreRepository> {
		DataStoreRepositoryImpl(
			context = androidContext()
		)
	}
}