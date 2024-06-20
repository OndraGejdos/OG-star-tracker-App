package og.ogstartracker.utils

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber

class CustomDebugTree : Timber.DebugTree() {

	override fun isLoggable(tag: String?, priority: Int): Boolean =
		priority >= Log.VERBOSE

	override fun createStackElementTag(element: StackTraceElement): String =
		"OgStackTracker " + super.createStackElementTag(element)

	@SuppressLint("LogNotTimber")
	override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
		when (priority) {
			Log.VERBOSE -> Log.v(tag, message, t)
			Log.DEBUG -> Log.d(tag, message, t)
			Log.INFO -> Log.i(tag, message, t)
			Log.WARN -> Log.w(tag, message, t)
			Log.ERROR -> Log.e(tag, message, t)
		}
	}
}