package og.ogstartracker.utils

import kotlinx.coroutines.flow.SharingStarted

/**
 * A [SharingStarted] meant to be used with a StateFlow to expose data to the UI.
 *
 * When the UI stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the UI stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 * save resources when the app is in the background but let users switch between apps quickly.
 *
 * Taken from: [CoroutinesUtils.kt](https://github.com/android/architecture-samples/blob/
 * 9575ea50e6683f8574b18eea60ff856a6f7e2c1c/app/src/main/java/com/example/android/architecture/blueprints/todoapp/
 * util/CoroutinesUtils.kt).
 */
private const val StopTimeoutMillis: Long = 5000
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)