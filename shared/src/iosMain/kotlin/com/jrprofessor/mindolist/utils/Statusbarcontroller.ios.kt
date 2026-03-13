package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable

@Composable
actual fun StatusBarLightMode() {
    // iOS: status bar style is controlled via Info.plist
    // UIViewControllerBasedStatusBarAppearance = false
    // UIStatusBarStyle = UIStatusBarStyleDarkContent
    // No runtime change needed here
}

@Composable
actual fun StatusBarDarkMode() {
    // iOS: set UIStatusBarStyle = UIStatusBarStyleLightContent in Info.plist
    // Or use setNeedsStatusBarAppearanceUpdate() via UIKit interop if needed
}