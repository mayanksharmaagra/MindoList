package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable

import com.jrprofessor.mindolist.di.PlatformBridgeHolder

@Composable
actual fun rememberGoogleSignInLauncher(
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    return {
        PlatformBridgeHolder.googleSignInBridge.signIn(
            onSuccess = { token -> onSuccess(token) },
            onError = { message -> onError(message) }
        )
    }
}
/*
@Composable
actual fun rememberGoogleSignInLauncher(
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    return {
        // iOS Native Google Sign In would be implemented here using GIDSignIn
        // For now, this is a placeholder
        onError("Google Sign-In is not yet implemented on iOS")
    }
}
*/
