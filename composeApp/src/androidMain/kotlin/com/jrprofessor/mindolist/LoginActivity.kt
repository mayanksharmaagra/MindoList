package com.jrprofessor.mindolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.jrprofessor.mindolist.navGraph.AppNavigation
import com.jrprofessor.mindolist.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        // ✅ Install splash screen FIRST
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        // ✅ Optional: Customize splash screen
        splashScreen.setKeepOnScreenCondition {
            // Return true to keep splash screen longer
            false
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {

}