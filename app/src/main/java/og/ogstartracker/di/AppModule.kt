package og.ogstartracker.di

import og.ogstartracker.ui.screens.DashboardViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

	viewModel {
		DashboardViewModel(
			context = androidContext()
		)
	}
}
