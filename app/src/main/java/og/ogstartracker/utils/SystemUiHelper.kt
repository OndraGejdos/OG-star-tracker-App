package og.ogstartracker.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

private const val SMALL_DELAY = 50L

@Composable
fun SystemUiHelper(
	statusIconsLight: Boolean = true,
	navigationIconsLight: Boolean = true,
) {
	val systemUiController = rememberSystemUiController()

	var trigger by remember {
		mutableStateOf(false)
	}

	// change with a small delay so it shows correctly
	// without this, the first screen in the app will have wrong settings
	if (trigger) {
		systemUiController.statusBarDarkContentEnabled = !statusIconsLight
		systemUiController.navigationBarDarkContentEnabled = !navigationIconsLight
		trigger = false
	}

	LaunchedEffect(Unit) {
		delay(SMALL_DELAY)
		trigger = true
	}
}

fun SystemUiController.set(
	statusIconsLight: Boolean = true,
	navigationIconsLight: Boolean = true,
) {
	statusBarDarkContentEnabled = !statusIconsLight
	navigationBarDarkContentEnabled = !navigationIconsLight
}
