package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun RequestMicrophonePermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LaunchedEffect(Unit) {
        // AI is disabled for iOS, so we just "grant" it conceptually or do nothing.
        onPermissionGranted()
    }
}
