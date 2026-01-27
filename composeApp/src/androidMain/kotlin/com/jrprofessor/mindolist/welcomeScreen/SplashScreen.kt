package com.jrprofessor.mindolist.welcomeScreen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.jrprofessor.mindolist.R

@Composable
fun SplashScreen() {
    StatusBarInDarkMode()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.ic_launcher_background))
            .safeContentPadding()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.welcome_logo),
                contentDescription = null
            )

            Text(
                text = stringResource(R.string.mindolist),
                fontSize = 26.sp,
                color = Color(0xFF6400CD),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.mind_to_do_list),
                fontSize = 16.sp,
                color = Color(0xFF667EEA),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}

@Composable
fun StatusBarInDarkMode() {
    val view = LocalView.current
    val activity = view.context as Activity

    SideEffect {
        val window = activity.window

        // Status bar background (optional but safe)
        window.statusBarColor = android.graphics.Color.WHITE

        // DARK icons on LIGHT background
        WindowCompat
            .getInsetsController(window, view)
            .isAppearanceLightStatusBars = true
    }
}
