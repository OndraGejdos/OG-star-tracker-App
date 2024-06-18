package og.ogstartracker.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

val DarkColorScheme = AppColorScheme(
	material = darkColorScheme(
		primary = ColorPrimary,
		secondary = ColorSecondary,
		tertiary = ColorBackground,
		background = ColorBackground,
		surface = ColorBackground
	),
	background = ColorBackground,
	primary = ColorPrimary,
	secondary = ColorSecondary,
	shadow = ColorShadow
)

// palette builder here https://m3.material.io/theme-builder#/custom
@Composable
fun AppTheme(content: @Composable () -> Unit) {
	// Remember a SystemUiController
	val systemUiController = rememberSystemUiController()

	DisposableEffect(systemUiController, false) {
		systemUiController.setSystemBarsColor(
			color = Color.Transparent,
			darkIcons = false,
			isNavigationBarContrastEnforced = false,
		)
		onDispose {}
	}

	val colors = DarkColorScheme

	CompositionLocalProvider(LocalColorScheme provides colors) {
		MaterialTheme(
			colorScheme = colors.material,
			typography = Typography,
			shapes = Shapes,
			content = content,
		)
	}
}

val LocalColorScheme = staticCompositionLocalOf<AppColorScheme> {
	error("No BananaColorScheme provided")
}

data class AppColorScheme constructor(
	val material: ColorScheme,
	val background: Color,
	val primary: Color,
	val secondary: Color,
	val shadow: Color
)

object AppTheme {

	/**
	 * Retrieves the current [BananaColorScheme] at the call site's position in the hierarchy.
	 */
	val colorScheme: AppColorScheme
		@Composable
		@ReadOnlyComposable
		get() = LocalColorScheme.current
}
