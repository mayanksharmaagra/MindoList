package com.jrprofessor.mindolist.presentation

sealed class SignUpIntent {
    data class NameChanged(val name: String) : SignUpIntent()
    data class EmailChanged(val email: String) : SignUpIntent()
    data class OtpChanged(val otp: String) : SignUpIntent()
    data class PasswordChanged(val password: String) : SignUpIntent()

    object ContinueWithEmailClicked : SignUpIntent()
    object CreateAccountClicked : SignUpIntent()
    object VerifyEmailClicked : SignUpIntent()
    object CreatePasswordClicked : SignUpIntent()

    object ResendOtpClicked : SignUpIntent()
    object BackPressed : SignUpIntent()
    object ErrorDismissed : SignUpIntent()
}