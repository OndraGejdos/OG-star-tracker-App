package og.ogstartracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import og.ogstartracker.ui.screens.DashboardScreen
import og.ogstartracker.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		setContent {
			AppTheme {
				val navController = rememberNavController()
				DashboardScreen(navController = navController)
			}
		}
	}
}
