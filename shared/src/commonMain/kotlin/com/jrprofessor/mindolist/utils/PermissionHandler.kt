package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable

@Composable
expect fun RequestMicrophonePermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
)
