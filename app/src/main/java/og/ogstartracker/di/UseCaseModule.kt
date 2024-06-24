package og.ogstartracker.di

import og.ogstartracker.domain.usecases.AbortCaptureUseCase
import og.ogstartracker.domain.usecases.DidUserSeeOnboardingUseCase
import og.ogstartracker.domain.usecases.GetCurrentHemisphereFlowUseCase
import og.ogstartracker.domain.usecases.GetCurrentStateUseCase
import og.ogstartracker.domain.usecases.GetSettingsUseCase
import og.ogstartracker.domain.usecases.SetCurrentHemisphereUseCase
import og.ogstartracker.domain.usecases.SetNewSettingsUseCase
import og.ogstartracker.domain.usecases.SetUserSawOnboardingUseCase
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

	single {
		GetCurrentHemisphereFlowUseCase(
			dataStoreRepository = get()
		)
	}

	single {
		SetCurrentHemisphereUseCase(
			dataStoreRepository = get()
		)
	}

	single {
		GetSettingsUseCase(
			dataStoreRepository = get()
		)
	}

	single {
		SetNewSettingsUseCase(
			dataStoreRepository = get()
		)
	}

	single {
		DidUserSeeOnboardingUseCase(
			repository = get()
		)
	}

	single {
		SetUserSawOnboardingUseCase(
			repository = get()
		)
	}
}