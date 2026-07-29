package com.jrprofessor.mindolist.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
actual fun rememberGoogleSignInLauncher(
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
): () -> Unit {
    val authManager = koinInject<GoogleAuthManager>()
    val androidAuthManager = authManager as? AndroidGoogleAuthManager
    val scope = rememberCoroutineScope()
    
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
                scope.launch {
                    val token = androidAuthManager.refreshAccessToken()
                    if (token != null) {
                        onSuccess(token)
                    } else {
                        // Fallback to idToken if refresh fails, but it might still 401
                        onSuccess(account.idToken ?: account.serverAuthCode ?: "")
                    }
                }
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
