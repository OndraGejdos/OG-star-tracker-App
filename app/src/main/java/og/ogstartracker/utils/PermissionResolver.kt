package og.ogstartracker.utils

import androidx.activity.ComponentActivity

object PermissionResolver {

	fun resolve(
		activity: ComponentActivity,
		permissions: Map<String, Boolean>,
		allGranted: () -> Unit,
		denied: (() -> Unit)? = null,
		permanentlyDenied: (() -> Unit)? = null
	) {
		when {
			permissions.values.all { it } -> allGranted.invoke()
			permissions.any {
				!it.value && !activity.shouldShowRequestPermissionRationale(it.key)
			} -> permanentlyDenied?.invoke()

			else -> denied?.invoke()
		}
	}
}