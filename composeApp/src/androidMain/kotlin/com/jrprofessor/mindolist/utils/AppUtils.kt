package com.jrprofessor.mindolist.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Patterns
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

fun validateEmail(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun validateOtp(code: String): Boolean = code.isNotBlank() && code.length == 5

fun validatePassword(password: String): Boolean {
    val hasMinLength = password.length >= 8
    val hasNumber = password.any { it.isDigit() }
    val hasSymbol = password.any { !it.isLetterOrDigit() }
    return hasMinLength && hasNumber && hasSymbol
}