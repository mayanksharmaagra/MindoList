package com.jrprofessor.mindolist.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import org.koin.compose.koinInject

@Composable
actual fun rememberGoogleSignInLauncher(
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val authManager = koinInject<GoogleAuthManager>()
    val androidAuthManager = authManager as? AndroidGoogleAuthManager
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (androidAuthManager == null) {
            onError("AndroidGoogleAuthManager not found")
            return@rememberLauncherForActivityResult
        }
        try {
            val account = androidAuthManager.handleResult(result.data)
            if (account != null) {
                // For simplicity, we use the serverAuthCode or idToken as "accessToken" 
                // In a real app, you'd exchange the serverAuthCode for a refresh/access token
                onSuccess(account.idToken ?: account.serverAuthCode ?: "")
            } else {
                onError("Sign in failed: No account returned")
            }
        } catch (e: Exception) {
            onError("Sign in failed: ${e.message}")
        }
    }

    return {
        androidAuthManager?.getSignInClient()?.signInIntent?.let {
            launcher.launch(it)
        } ?: onError("Google Sign-In not available")
    }
}
