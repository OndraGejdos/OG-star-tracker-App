package og.ogstartracker.di

import og.ogstartracker.domain.usecases.AbortCaptureUseCase
import og.ogstartracker.domain.usecases.GetCurrentStateUseCase
import og.ogstartracker.domain.usecases.StartCaptureUseCase
import og.ogstartracker.domain.usecases.StartSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.StopSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.TurnTrackerLeftUseCase
import og.ogstartracker.domain.usecases.TurnTrackerRightUseCase
import org.koin.dsl.module

val useCaseModule = module {

	single {
		StartSiderealTrackingUseCase(
			repository = get()
		)
	}

	single {
		StopSiderealTrackingUseCase(
			repository = get()
		)
	}

	single {
		TurnTrackerLeftUseCase(
			repository = get()
		)
	}

	single {
		TurnTrackerRightUseCase(
			repository = get()
		)
	}

	single {
		StartCaptureUseCase(
			repository = get()
		)
	}

	single {
		AbortCaptureUseCase(
			repository = get()
		)
	}

	single {
		GetCurrentStateUseCase(
			repository = get()
		)
	}
}