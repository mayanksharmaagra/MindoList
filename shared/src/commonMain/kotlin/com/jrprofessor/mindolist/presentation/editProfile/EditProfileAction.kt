package com.jrprofessor.mindolist.presentation.editProfile

sealed class EditProfileAction {
    data class OnFullNameChange(val fullName: String) : EditProfileAction()
    data class OnEmailChange(val email: String) : EditProfileAction()
    data class OnCurrentPasswordChange(val password: String) : EditProfileAction()
    data class OnNewPasswordChange(val password: String) : EditProfileAction()
    data class OnConfirmPasswordChange(val password: String) : EditProfileAction()
    data class OnAvatarChange(val bytes: ByteArray) : EditProfileAction()
    data object OnSaveClick : EditProfileAction()
}
