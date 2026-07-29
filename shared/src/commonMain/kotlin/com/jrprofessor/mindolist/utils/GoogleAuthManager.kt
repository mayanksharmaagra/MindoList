package com.jrprofessor.mindolist.utils

import com.jrprofessor.mindolist.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface GoogleAuthManager {
    val userData: StateFlow<User?>
    fun signOut()
    suspend fun refreshAccessToken(oldToken: String? = null): String?
}
