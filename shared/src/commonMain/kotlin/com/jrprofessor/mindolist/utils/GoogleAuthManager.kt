package com.jrprofessor.mindolist.utils

import kotlinx.coroutines.flow.StateFlow

data class GoogleUserData(
    val email: String,
    val accessToken: String,
    val displayName: String?
)

interface GoogleAuthManager {
    val userData: StateFlow<GoogleUserData?>
    fun signOut()
}


