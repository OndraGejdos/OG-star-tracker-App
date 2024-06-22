package og.ogstartracker.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val CLOSE_DELAY = 100L

@OptIn(ExperimentalMaterial3Api::class)
fun closeBottomSheet(
	scope: CoroutineScope,
	bottomSheetState: SheetState,
	onHide: () -> Unit,
	doOnClose: (() -> Unit)? = null,
) {
	scope.launch {
		bottomSheetState.hide()
		delay(CLOSE_DELAY)
	}.invokeOnCompletion {
		doOnClose?.invoke()
		onHide()
	}
}