package com.jrprofessor.mindolist.presentation

sealed class LoginIntent {
    // Initial screen
    object ContinueWithEmailClicked : LoginIntent()

    // Email/Password screen
    data class EmailChanged(val email: String) : LoginIntent()
    data class PasswordChanged(val password: String) : LoginIntent()
    object TogglePasswordVisibility : LoginIntent()
    object LoginClicked : LoginIntent()
    object ForgotPasswordClicked : LoginIntent()

    // Navigation
    object BackPressed : LoginIntent()

    // Error
    object ErrorDismissed : LoginIntent()
}