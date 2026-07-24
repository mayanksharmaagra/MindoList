package com.jrprofessor.mindolist.presentation.signup

sealed class SignUpIntent {
    data class NameChanged(val name: String) : SignUpIntent()
    data class EmailChanged(val email: String) : SignUpIntent()
    data class OtpChanged(val otp: String) : SignUpIntent()
    data class PasswordChanged(val password: String) : SignUpIntent()
    data class UserProfileUrl(val url: ByteArray,val email: String) : SignUpIntent()

//    object ContinueWithEmailClicked : SignUpIntent()
    object SendVerificationCode : SignUpIntent()
    object VerifyEmailClicked : SignUpIntent()
//    object CreatePasswordClicked : SignUpIntent()

    object ResendOtpClicked : SignUpIntent()
    object BackPressed : SignUpIntent()
    object ErrorDismissed : SignUpIntent()
    object ClearState : SignUpIntent()
}