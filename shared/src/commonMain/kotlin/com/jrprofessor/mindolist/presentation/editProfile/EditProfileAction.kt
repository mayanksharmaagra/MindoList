package com.jrprofessor.mindolist.presentation.editProfile

sealed class EditProfileAction {
    data class OnFullNameChange(val fullName: String) : EditProfileAction()
    data class OnEmailChange(val email: String) : EditProfileAction()
    data class OnPhoneNumberChange(val phoneNumber: String) : EditProfileAction()
    data class OnAboutChange(val about: String) : EditProfileAction()
    data class OnAvatarChange(val bytes: ByteArray) : EditProfileAction()
    data object OnRemovePhoto : EditProfileAction()
    data object OnSaveClick : EditProfileAction()
    
    // Security Actions
    data object OnChangePasswordClick : EditProfileAction()
    data object OnTwoFactorClick : EditProfileAction()
    data object OnDeleteAccountClick : EditProfileAction()
}
