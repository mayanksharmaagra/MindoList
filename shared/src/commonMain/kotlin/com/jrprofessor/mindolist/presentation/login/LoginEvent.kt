package com.jrprofessor.mindolist.presentation.login

sealed class LoginEvent {
    // Navigation
    object NavigateToHome : LoginEvent()
    object NavigateToForgotPassword : LoginEvent()
    object NavigateBack : LoginEvent()

    // Feedback
    data class ShowToast(val message: String) : LoginEvent()
    data class ShowError(val error: String) : LoginEvent()
}