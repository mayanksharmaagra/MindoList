package com.jrprofessor.mindolist.utils

interface GoogleSignInBridge {
    fun signIn(onSuccess: (String) -> Unit, onError: (String) -> Unit)
}