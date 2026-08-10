package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

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

@Composable
actual fun RequestNotificationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    LaunchedEffect(Unit) {
        val center = UNUserNotificationCenter.currentNotificationCenter()
        val options = UNAuthorizationOptionAlert or
                UNAuthorizationOptionSound or
                UNAuthorizationOptionBadge
        
        println("iOS: Checking notification settings...")
        
        center.getNotificationSettingsWithCompletionHandler { settings ->
            platform.darwin.dispatch_async(platform.darwin.dispatch_get_main_queue()) {
                val status = settings?.authorizationStatus ?: 0L
                println("iOS: Notification status: $status")
                
                if (status == platform.UserNotifications.UNAuthorizationStatusDenied) {
                    showToast("Please enable notifications in iOS Settings to use reminders.")
                    onPermissionDenied()
                } else {
                    println("iOS: Requesting authorization...")
                    center.requestAuthorizationWithOptions(options) { granted, error ->
                        platform.darwin.dispatch_async(platform.darwin.dispatch_get_main_queue()) {
                            if (error != null) {
                                println("iOS: Notification auth error: ${error.localizedDescription}")
                            }
                            println("iOS: Notification auth result: $granted")
                            if (granted && error == null) {
                                onPermissionGranted()
                            } else {
                                onPermissionDenied()
                            }
                        }
                    }
                }
            }
        }
    }
}
