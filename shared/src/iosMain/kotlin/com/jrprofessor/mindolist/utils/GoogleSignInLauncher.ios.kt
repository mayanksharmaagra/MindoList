package com.jrprofessor.mindolist.utils

import androidx.compose.runtime.Composable

import com.jrprofessor.mindolist.di.IosGoogleAuthManager
import com.jrprofessor.mindolist.di.PlatformBridgeHolder
import com.jrprofessor.mindolist.domain.model.User
import org.koin.compose.koinInject

@Composable
actual fun rememberGoogleSignInLauncher(
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val authManager = koinInject<GoogleAuthManager>()
    val iosAuthManager = authManager as? IosGoogleAuthManager
    
    return {
        PlatformBridgeHolder.googleSignInBridge.signIn(
            onSuccess = { token, email ->
                iosAuthManager?.updateUserData(
                    User(
                        email = email,
                        googleEmail = email,
                        googleAccessToken = token,
                        isGoogleConnected = true
                    )
                )
                onSuccess(token)
            },
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
