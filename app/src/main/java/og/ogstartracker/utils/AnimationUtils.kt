package og.ogstartracker.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

fun slideEnterAnimation(
	durationInMillis: Int = 300,
): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
	slideIntoContainer(
		AnimatedContentTransitionScope.SlideDirection.Left,
		animationSpec = tween(durationInMillis),
	)
}

fun slideExitAnimation(
	durationInMillis: Int = 300,
): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
	slideOutOfContainer(
		AnimatedContentTransitionScope.SlideDirection.Left,
		animationSpec = tween(durationInMillis),
	)
}

fun slidePopEnterAnimation(
	durationInMillis: Int = 300,
): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition) = {
	slideIntoContainer(
		AnimatedContentTransitionScope.SlideDirection.Right,
		animationSpec = tween(durationInMillis),
	)
}

fun slidePopExitAnimation(
	durationInMillis: Int = 300,
): (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition) = {
	slideOutOfContainer(
		AnimatedContentTransitionScope.SlideDirection.Right,
		animationSpec = tween(durationInMillis),
	)
}