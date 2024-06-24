package og.ogstartracker.di

import og.ogstartracker.domain.usecases.arduino.AbortCaptureUseCase
import og.ogstartracker.domain.usecases.onboarding.DidUserSeeOnboardingUseCase
import og.ogstartracker.domain.usecases.settings.GetCurrentHemisphereFlowUseCase
import og.ogstartracker.domain.usecases.arduino.GetCurrentStateUseCase
import og.ogstartracker.domain.usecases.settings.GetSettingsUseCase
import og.ogstartracker.domain.usecases.settings.SetCurrentHemisphereUseCase
import og.ogstartracker.domain.usecases.settings.SetNewSettingsUseCase
import og.ogstartracker.domain.usecases.onboarding.SetUserSawOnboardingUseCase
import og.ogstartracker.domain.usecases.arduino.StartCaptureUseCase
import og.ogstartracker.domain.usecases.arduino.StartSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.arduino.StopSiderealTrackingUseCase
import og.ogstartracker.domain.usecases.arduino.TurnTrackerLeftUseCase
import og.ogstartracker.domain.usecases.arduino.TurnTrackerRightUseCase
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