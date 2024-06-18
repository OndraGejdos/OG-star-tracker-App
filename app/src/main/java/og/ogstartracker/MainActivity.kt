package og.ogstartracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import og.ogstartracker.ui.components.CardBase
import og.ogstartracker.ui.components.SiderealCard
import og.ogstartracker.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			AppTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
					Column {
						CardBase(icon = R.drawable.ic_plus, title = "Title", subtitle = "Subtitle")
						CardBase(icon = R.drawable.ic_plus, title = "Title", subtitle = "Subtitle")
						SiderealCard(active = false, onCheckChanged = {})
						CardBase(icon = R.drawable.ic_plus, title = "Title", subtitle = "Subtitle")
					}
				}
			}
		}
	}
}
