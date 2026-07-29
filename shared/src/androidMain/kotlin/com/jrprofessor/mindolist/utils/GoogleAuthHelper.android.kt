package com.jrprofessor.mindolist.utils

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.shared.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AndroidGoogleAuthManager(private val context: Context) : GoogleAuthManager {

    private val _userData = MutableStateFlow<User?>(null)
    override val userData: StateFlow<User?> = _userData.asStateFlow()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            _userData.value = User(
                email = account.email ?: "",
                googleEmail = account.email,
                googleAccessToken = account.idToken ?: account.serverAuthCode ?: "",
                displayName = account.displayName,
                isGoogleConnected = true
            )
            // Fetch real access token in background
            scope.launch {
                refreshAccessToken()
            }
        }
    }

    override suspend fun refreshAccessToken(oldToken: String?): String? = withContext(Dispatchers.IO) {
        try {
            val account = GoogleSignIn.getLastSignedInAccount(context) ?: return@withContext null
            
            oldToken?.let {
                GoogleAuthUtil.invalidateToken(context, it)
                Logger.debug { "Google Access Token invalidated" }
            }

            val scopes = "oauth2:https://www.googleapis.com/auth/calendar.readonly https://www.googleapis.com/auth/tasks.readonly"
            val token = GoogleAuthUtil.getToken(context, account.account!!, scopes)
            
            Logger.debug { "Google Access Token fetched: ${token.take(10)}..." }
            
            withContext(Dispatchers.Main) {
                _userData.value = _userData.value?.copy(
                    googleAccessToken = token,
                    isGoogleConnected = true
                )
            }
            token
        } catch (e: Exception) {
            Logger.error { "Failed to refresh Google access token: ${e.message}" }
            null
        }
    }

    fun getSignInClient(): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope("https://www.googleapis.com/auth/calendar.readonly"),
                Scope("https://www.googleapis.com/auth/tasks.readonly")
            )
            .requestServerAuthCode(context.getString(R.string.server_client_id))
            .requestIdToken(context.getString(R.string.server_client_id))
            .build()

        return GoogleSignIn.getClient(context, options)
    }

    override fun signOut() {
        getSignInClient().signOut()
        _userData.value = null
    }

    fun handleResult(data: Intent?): GoogleSignInAccount? {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.result
        if (account != null) {
            _userData.value = User(
                email = account.email ?: "",
                googleEmail = account.email,
                googleAccessToken = account.idToken ?: account.serverAuthCode ?: "", 
                displayName = account.displayName,
                isGoogleConnected = true
            )
            // Note: handleResult is called from UI, so we launch background token fetch
            scope.launch {
                refreshAccessToken()
            }
        }
        return account
    }
}
