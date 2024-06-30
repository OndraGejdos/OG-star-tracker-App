package og.ogstartracker.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import me.zhanghai.compose.preference.Preference
import me.zhanghai.compose.preference.rememberPreferenceState
import og.ogstartracker.ui.components.common.CustomSwitch
import og.ogstartracker.ui.theme.DimensNormal100

inline fun LazyListScope.switchPreferences(
	key: String,
	defaultValue: Boolean,
	crossinline title: @Composable (Boolean) -> Unit,
	modifier: Modifier = Modifier.fillMaxWidth(),
	crossinline rememberState: @Composable () -> MutableState<Boolean> = {
		rememberPreferenceState(key, defaultValue)
	},
	crossinline enabled: (Boolean) -> Boolean = { true },
	noinline icon: @Composable ((Boolean) -> Unit)? = null,
	noinline summary: @Composable ((Boolean) -> Unit)? = null
) {
	item(key = key, contentType = "SwitchPreference") {
		val state = rememberState()
		val value by state
		SwitchPreference(
			state = state,
			title = { title(value) },
			modifier = modifier,
			enabled = enabled(value),
			icon = icon?.let { { it(value) } },
			summary = summary?.let { { it(value) } }
		)
	}
}

@Composable
fun SwitchPreference(
	state: MutableState<Boolean>,
	title: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	icon: @Composable (() -> Unit)? = null,
	summary: @Composable (() -> Unit)? = null
) {
	var value by state
	SwitchPreference(
		value = value,
		onValueChange = { value = it },
		title = title,
		modifier = modifier,
		enabled = enabled,
		icon = icon,
		summary = summary
	)
}

@Composable
fun SwitchPreference(
	value: Boolean,
	onValueChange: (Boolean) -> Unit,
	title: @Composable () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	icon: @Composable (() -> Unit)? = null,
	summary: @Composable (() -> Unit)? = null
) {
	Preference(
		title = title,
		modifier = modifier.toggleable(value, enabled, Role.Switch, onValueChange),
		enabled = enabled,
		icon = icon,
		summary = summary,
		widgetContainer = {
			CustomSwitch(
				checked = value,
				onCheckChange = onValueChange,
				enabled = enabled,
				modifier = Modifier.padding(end = DimensNormal100)
			)
		}
	)
}