package com.jrprofessor.mindolist.utils

import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.jrprofessor.mindolist.shared.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AndroidGoogleAuthManager(private val context: Context) : GoogleAuthManager {

    private val _userData = MutableStateFlow<GoogleUserData?>(null)
    override val userData: StateFlow<GoogleUserData?> = _userData.asStateFlow()

    fun getSignInClient(): GoogleSignInClient {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestScopes(
                Scope("https://www.googleapis.com/auth/calendar.readonly"),
                Scope("https://www.googleapis.com/auth/tasks.readonly")
            )
            .requestServerAuthCode(context.getString(R.string.server_client_id))
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
            _userData.value = GoogleUserData(
                email = account.email ?: "",
                accessToken = account.idToken ?: "", // Usually you'd get the access token from the auth code
                displayName = account.displayName
            )
        }
        return account
    }
}

