package com.nailmind.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.nailmind.app.ui.NailMindApp
import com.nailmind.app.ui.theme.NailMindTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.WHITE
        window.navigationBarColor = Color.WHITE
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        setContent {
            NailMindTheme {
                NailMindApp()
            }
        }
    }
}
