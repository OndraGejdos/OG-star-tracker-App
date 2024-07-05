package og.ogstartracker.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import dev.jeziellago.compose.markdowntext.MarkdownText
import og.ogstartracker.R
import og.ogstartracker.ui.theme.AppTheme
import og.ogstartracker.ui.theme.DimensNormal100
import og.ogstartracker.ui.theme.DimensNormal75
import og.ogstartracker.ui.theme.textStyle16Bold

@Composable
fun InfoDialog(
	modifier: Modifier = Modifier,
	onHide: () -> Unit,
	isCancelable: Boolean = true,
) {
	AlertDialog(
		properties = DialogProperties(
			usePlatformDefaultWidth = false,
			decorFitsSystemWindows = false
		),
		modifier = modifier
			.fillMaxWidth()
			.padding(DimensNormal100)
			.clip(RoundedCornerShape(DimensNormal75)),
		onDismissRequest = {
			if (isCancelable) {
				onHide()
			}
		},
		containerColor = AppTheme.colorScheme.shadow,
		titleContentColor = Color.White,
		iconContentColor = Color.White,
		textContentColor = Color.White,
		title = {
			Text(
				modifier = Modifier
					.fillMaxWidth(),
				textAlign = TextAlign.Center,
				text = "Welcome to OG Star Tracker",
			)
		},
		text = {
			DialogContent()
		},
		dismissButton = {
			Button(
				onClick = {
					if (isCancelable) {
						onHide()
					}
				},
				colors = ButtonDefaults.buttonColors(
					containerColor = AppTheme.colorScheme.primary,
					contentColor = AppTheme.colorScheme.background,
					disabledContainerColor = AppTheme.colorScheme.primary.copy(alpha = 0.5f),
					disabledContentColor = AppTheme.colorScheme.background
				),
			) {
				Text(
					text = "Close".uppercase(),
					style = textStyle16Bold,
					color = AppTheme.colorScheme.background
				)
			}
		},
		confirmButton = {},
	)
}

@Composable
fun DialogContent(modifier: Modifier = Modifier) {
	val pageSource = """
		Thank you for downloading the Star Tracker mobile app. This app is designed to enhance your stargazing experience by integrating with our specialized Star Tracker product. Please note that to fully utilize the features of this app, you will need to own the Star Tracker hardware available at [OG Star Tech](https://www.ogstartech.com/home/).

		## Open Source for the Community

		Star Tracker is an open-source project, meaning all aspects of the app, as well as the hardware, are freely available for modification and improvement by the community. We believe in the power of collaboration and transparency. You can access the source code, contribute, or simply see how it all works by visiting our GitHub repository at [OG Star Tracker Repo](https://github.com/OndraGejdos/OG-star-tracker-).

		## About the Author

		This innovative project is brought to you by Ondřej Gejdoš, a passionate astronomer and developer dedicated to making stargazing more accessible and enjoyable for everyone.

		## Understanding Equatorial Mounts

		To make the most of your Star Tracker, it's important to understand how equatorial mounts work. An equatorial mount is designed to follow the rotation of the Earth. By aligning the mount's axis with the Earth's axis of rotation, it allows you to track celestial objects accurately by compensating for the Earth's movement. This means you can keep a star or planet in your viewfinder for longer periods, making it ideal for both observational astronomy and astrophotography.

		## Getting Started

		1. **Setup Your Hardware:** Ensure your Star Tracker is properly assembled and aligned. Refer to the hardware manual for detailed instructions.
		2. **Connect Your App:** Open the Star Tracker app and connect it to your Star Tracker hardware via Wi-Fi.
		3. **Calibrate:** Use the app to calibrate your mount. Follow the step-by-step guide within the app to ensure precise alignment.
		4. **Start Tracking:** Select the celestial object you wish to observe or photograph, and let Star Tracker do the rest. Enjoy seamless tracking and enhanced viewing.

		We hope you enjoy using Star Tracker. Clear skies and happy stargazing!

	""".trimIndent()

	MarkdownText(
		modifier = modifier,
		markdown = pageSource,
		fontResource = R.font.inter_regular,
		style = textStyle16Bold
	)
}

@Preview
@Composable
fun DialogContentPreview() {
	DialogContent()
}