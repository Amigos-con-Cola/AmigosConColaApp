package io.github.amigosconcola

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import io.github.amigosconcola.ui.screens.MainScreen
import io.github.amigosconcola.ui.theme.AmigosConColaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmigosConColaTheme {
                MainScreen()
            }
        }
    }
}