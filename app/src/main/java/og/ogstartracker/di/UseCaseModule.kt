package og.ogstartracker.di

import og.ogstartracker.domain.usecases.StartSiderealTrackingUseCase
import org.koin.dsl.module

val useCaseModule = module {

	single {
		StartSiderealTrackingUseCase(
			repository = get()
		)
	}
}