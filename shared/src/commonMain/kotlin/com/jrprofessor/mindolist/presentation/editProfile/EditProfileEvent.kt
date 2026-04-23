package com.jrprofessor.mindolist.presentation.editProfile

sealed class EditProfileEvent {
    data class ShowToast(val message: String) : EditProfileEvent()
    data object NavigateBack : EditProfileEvent()
}
