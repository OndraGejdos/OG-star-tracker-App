package og.ogstartracker.domain.models

import androidx.annotation.StringRes
import og.ogstartracker.R

enum class Hemisphere constructor(
	@StringRes val text: Int
) {
	NORTH(R.string.settings_north),
	SOUTH(R.string.settings_south)
}