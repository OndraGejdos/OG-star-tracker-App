package og.ogstartracker.domain.models

import androidx.annotation.StringRes
import og.ogstartracker.R

enum class Hemisphere constructor(
	val arduinoValue: Int,
	@StringRes val text: Int
) {
	NORTH(1, R.string.settings_north),
	SOUTH(0, R.string.settings_south)
}