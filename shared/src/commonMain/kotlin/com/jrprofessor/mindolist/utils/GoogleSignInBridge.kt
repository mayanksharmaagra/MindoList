package com.jrprofessor.mindolist.utils

interface GoogleSignInBridge {
    fun signIn(onSuccess: (String, String) -> Unit, onError: (String) -> Unit)
    fun refreshAccessToken(onSuccess: (String) -> Unit, onError: (String) -> Unit)
}