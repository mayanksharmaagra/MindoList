package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable

/**
 * Sets status bar to light mode (dark icons on light background)
 * Android: uses WindowCompat
 * iOS: no-op (handled via Info.plist / UIStatusBarStyle)
 */
@Composable
expect fun StatusBarLightMode()

/**
 * Sets status bar to dark mode (light icons on dark background)
 */
@Composable
expect fun StatusBarDarkMode()