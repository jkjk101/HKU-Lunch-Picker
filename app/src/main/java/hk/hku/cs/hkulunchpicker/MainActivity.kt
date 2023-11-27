package hk.hku.cs.hkulunchpicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import hk.hku.cs.hkulunchpicker.ui.theme.HKULunchPickerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val landing = intent.getStringExtra("landing") ?: "Home"

        setContent {
            HKULunchPickerTheme {
                MainNav(landing)
            }
        }
    }
}