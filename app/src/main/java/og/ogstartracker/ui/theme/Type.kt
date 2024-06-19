package og.ogstartracker.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import og.ogstartracker.R

val interAppFontFamily = FontFamily(
	Font(R.font.inter_thin, FontWeight.Thin),
	Font(R.font.inter_extralight, FontWeight.ExtraLight),
	Font(R.font.inter_light, FontWeight.Light),
	Font(R.font.inter_regular, FontWeight.Normal),
	Font(R.font.inter_medium, FontWeight.Medium),
	Font(R.font.inter_semibold, FontWeight.SemiBold),
	Font(R.font.inter_bold, FontWeight.Bold),
	Font(R.font.inter_extrabold, FontWeight.ExtraBold),
	Font(R.font.inter_black, FontWeight.Black),
)

// Set of Material typography styles to start with
val Typography = Typography(
	bodyLarge = TextStyle(
		fontFamily = FontFamily.Default,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		lineHeight = 24.sp,
		letterSpacing = 0.5.sp
	)
)

val textStyle16Regular = TextStyle(
	fontWeight = FontWeight.Normal,
	fontSize = 16.sp,
	fontFamily = interAppFontFamily,
)

val textStyle16Bold = textStyle16Regular.copy(fontWeight = FontWeight.Bold)

val textStyle24Bold = TextStyle(
	fontWeight = FontWeight.Bold,
	fontSize = 24.sp,
	fontFamily = interAppFontFamily,
)

val textStyle20Bold = TextStyle(
	fontWeight = FontWeight.Bold,
	fontSize = 20.sp,
	fontFamily = interAppFontFamily,
)

val textStyle10Bold = TextStyle(
	fontWeight = FontWeight.Bold,
	fontSize = 10.sp,
	fontFamily = interAppFontFamily,
)

val textStyle12Bold = TextStyle(
	fontWeight = FontWeight.Bold,
	fontSize = 12.sp,
	fontFamily = interAppFontFamily,
)

val textStyle14Bold = TextStyle(
	fontWeight = FontWeight.Bold,
	fontSize = 14.sp,
	fontFamily = interAppFontFamily,
)

val textStyle10ItalicBold = textStyle10Bold.copy(fontStyle = FontStyle.Italic)
