package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun RequestMicrophonePermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LaunchedEffect(Unit) {
        onPermissionGranted()
    }
}

@Composable
actual fun RequestCameraPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LaunchedEffect(Unit) {
        onPermissionGranted()
    }
}

@Composable
actual fun RequestStoragePermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LaunchedEffect(Unit) {
        onPermissionGranted()
    }
}
