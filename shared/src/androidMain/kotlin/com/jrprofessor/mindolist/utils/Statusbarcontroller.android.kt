package com.jrprofessor.mindolist.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
actual fun StatusBarLightMode() {
    val view = LocalView.current
    val context = LocalContext.current
    val activity = context.findActivity()

    SideEffect {
        activity?.window?.let { window ->
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = true  // dark icons
        }
    }
}

@Composable
actual fun StatusBarDarkMode() {
    val view = LocalView.current
    val context = LocalContext.current
    val activity = context.findActivity()

    SideEffect {
        activity?.window?.let { window ->
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = false // light icons
        }
    }
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
