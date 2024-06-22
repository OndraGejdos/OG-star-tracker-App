package og.ogstartracker.di

import og.ogstartracker.ui.screens.DashboardViewModel
import og.ogstartracker.ui.screens.SettingsViewModel
import og.ogstartracker.utils.VibratorController
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

	viewModel {
		DashboardViewModel(
			vibratorController = get(),
			startSiderealTracking = get(),
			stopSiderealTracking = get(),
			trackerLeft = get(),
			trackerRight = get(),
			startCapture = get(),
			abortCapture = get(),
			getCurrentHemisphereFlow = get()
		)
	}

	viewModel {
		SettingsViewModel(
			setCurrentHemisphere = get(),
			getCurrentHemisphereFlow = get()
		)
	}

	single {
		VibratorController(
			context = androidContext()
		)
	}
}
