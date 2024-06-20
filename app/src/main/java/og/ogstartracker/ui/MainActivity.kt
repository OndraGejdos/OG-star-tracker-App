package og.ogstartracker.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import og.ogstartracker.R
import og.ogstartracker.ui.components.common.ProvidesInsets
import og.ogstartracker.ui.screens.DashboardScreen
import og.ogstartracker.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

	private lateinit var splashScreen: SplashScreen

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setSplashScreen()

		enableEdgeToEdge()
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

		setContent {
			AppTheme {
				ProvidesInsets {
					val navController = rememberNavController()
					DashboardScreen(navController = navController)
				}
			}
		}
	}

	private fun setSplashScreen() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			splashScreen = installSplashScreen()
			splashScreen.setKeepOnScreenCondition { true }
		} else {
			setTheme(R.style.Theme_App_Starting_Legacy)
		}

		lifecycleScope.launch {
			delay(2000)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				splashScreen.setKeepOnScreenCondition { false }
			} else {
				window?.setBackgroundDrawableResource(R.drawable.background)
			}
		}
	}
}
