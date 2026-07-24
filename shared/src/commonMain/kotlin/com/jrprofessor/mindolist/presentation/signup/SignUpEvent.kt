package com.jrprofessor.mindolist.presentation.signup

sealed class SignUpEvent {
    object NavigateBack : SignUpEvent()
    object NavigateToHome : SignUpEvent()
    object NavigateToVerifyEmail : SignUpEvent()
    data class ShowToast(val message: String) : SignUpEvent()
    data class ShowError(val error: String) : SignUpEvent()
}