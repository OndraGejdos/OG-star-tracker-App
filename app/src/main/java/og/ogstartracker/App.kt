package og.ogstartracker

import android.app.Application
import og.ogstartracker.di.appModule
import og.ogstartracker.di.networkModule
import og.ogstartracker.di.repositoryModule
import og.ogstartracker.di.useCaseModule
import og.ogstartracker.utils.CustomDebugTree
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

	override fun onCreate() {
		super.onCreate()

		Timber.plant(CustomDebugTree())

		startKoin {
			androidLogger(Level.ERROR)
			androidContext(this@App)
			modules(
				listOf(
					appModule,
					networkModule,
					useCaseModule,
					repositoryModule
				)
			)
		}
	}
}