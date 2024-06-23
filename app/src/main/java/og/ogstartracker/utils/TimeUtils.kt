package og.ogstartracker.utils

import kotlin.time.Duration.Companion.milliseconds

fun Long.toHours() = this.milliseconds.inWholeHours

fun Long.toMinutes() = this.milliseconds.inWholeMinutes - this.toHours() * 60

fun Long.toSeconds() = this.milliseconds.inWholeSeconds - this.toHours() * 3600 - this.toMinutes() * 60