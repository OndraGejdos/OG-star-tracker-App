package og.ogstartracker.ui.components.common

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Insets constructor(
	val navigationBarInset: Dp = 0.dp,
	val statusBarInset: Dp = 0.dp
) {
	fun all(): Dp = navigationBarInset + statusBarInset
}

val LocalInsets = compositionLocalOf { Insets() }

@Composable
fun ProvidesInsets(content: @Composable () -> Unit) {
	CompositionLocalProvider(
		LocalInsets provides Insets(
			navigationBarInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding(),
			statusBarInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
		)
	) {
		content()
	}
}