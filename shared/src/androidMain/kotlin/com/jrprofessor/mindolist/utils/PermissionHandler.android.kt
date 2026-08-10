package com.jrprofessor.mindolist.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
actual fun RequestMicrophonePermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    RequestPermissionInternal(
        permission = Manifest.permission.RECORD_AUDIO,
        onPermissionGranted = onPermissionGranted,
        onPermissionDenied = onPermissionDenied
    )
}

@Composable
actual fun RequestCameraPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    RequestPermissionInternal(
        permission = Manifest.permission.CAMERA,
        onPermissionGranted = onPermissionGranted,
        onPermissionDenied = onPermissionDenied
    )
}

@Composable
actual fun RequestStoragePermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    RequestPermissionInternal(
        permission = permission,
        onPermissionGranted = onPermissionGranted,
        onPermissionDenied = onPermissionDenied
    )
}

@Composable
actual fun RequestNotificationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        RequestPermissionInternal(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onPermissionGranted = onPermissionGranted,
            onPermissionDenied = onPermissionDenied
        )
    } else {
        onPermissionGranted()
    }
}

@Composable
private fun RequestPermissionInternal(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(permission) {
        val permissionCheck = ContextCompat.checkSelfPermission(context, permission)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionGranted()
        } else {
            launcher.launch(permission)
        }
    }
}
