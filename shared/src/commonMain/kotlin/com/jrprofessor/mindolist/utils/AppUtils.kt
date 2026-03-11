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
fun StatusBarInDarkMode() {
    val view = LocalView.current
    val context = LocalContext.current
    val activity = context.findActivity()

    SideEffect {
        activity?.window?.let { window ->
            // Status bar background (optional but safe)
            window.statusBarColor = android.graphics.Color.WHITE

            // DARK icons on LIGHT background
            WindowCompat
                .getInsetsController(window, view)
                .isAppearanceLightStatusBars = true
        }
    }
}

private fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}