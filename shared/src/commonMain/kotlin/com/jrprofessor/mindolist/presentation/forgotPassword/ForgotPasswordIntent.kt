package com.jrprofessor.mindolist.presentation.forgotPassword

sealed class ForgotPasswordIntent {
    // Email/Password screen
    data class EmailChanged(val email: String) : ForgotPasswordIntent()
    data class PasswordChanged(val password: String) : ForgotPasswordIntent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : ForgotPasswordIntent()
    data class OtpChanged(val otp: String) : ForgotPasswordIntent()

    // Navigation
    object BackPressed : ForgotPasswordIntent()
    object ContinueWithEmailClicked : ForgotPasswordIntent()
    object ContinueWithEmailVerifyClicked : ForgotPasswordIntent()
    object ContinueWithUpdatePassword : ForgotPasswordIntent()

    // Error
    object ErrorDismissed : ForgotPasswordIntent()
    object ResendOtpClicked : ForgotPasswordIntent()

}