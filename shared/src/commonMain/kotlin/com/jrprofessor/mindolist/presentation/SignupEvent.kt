package com.jrprofessor.mindolist.presentation

sealed class SignUpEvent {
    object NavigateBack : SignUpEvent()
    object NavigateToHome : SignUpEvent()
    data class ShowToast(val message: String) : SignUpEvent()
    data class ShowError(val error: String) : SignUpEvent()
}