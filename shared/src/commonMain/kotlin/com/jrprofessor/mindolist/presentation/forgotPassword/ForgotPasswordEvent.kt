package com.jrprofessor.mindolist.presentation.forgotPassword

sealed class ForgotPasswordEvent {
    object NavigateBack : ForgotPasswordEvent()
    data class ShowToast(val message: String) : ForgotPasswordEvent()
    data class ShowError(val error: String) : ForgotPasswordEvent()
}