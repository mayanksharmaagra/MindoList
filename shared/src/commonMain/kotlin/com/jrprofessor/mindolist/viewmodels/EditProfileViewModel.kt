package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileAction
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileEvent
import com.jrprofessor.mindolist.presentation.editProfile.EditProfileState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditProfileEvent>()
    val event: SharedFlow<EditProfileEvent> = _event.asSharedFlow()

    private var currentUser: User? = null

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            firebaseAuthRepository.getCurrentUser().collect { user ->
                currentUser = user
                user?.let {
                    _state.update { currentState ->
                        currentState.copy(
                            fullName = it.displayName ?: "",
                            email = it.email,
                            avatarUrl = it.profileUrl
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: EditProfileAction) {
        when (action) {
            is EditProfileAction.OnFullNameChange -> {
                _state.update { it.copy(fullName = action.fullName) }
            }
            is EditProfileAction.OnEmailChange -> {
                _state.update { it.copy(email = action.email) }
            }
            is EditProfileAction.OnCurrentPasswordChange -> {
                _state.update { it.copy(currentPassword = action.password) }
            }
            is EditProfileAction.OnNewPasswordChange -> {
                _state.update { it.copy(newPassword = action.password) }
            }
            is EditProfileAction.OnConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = action.password) }
            }
            is EditProfileAction.OnAvatarChange -> {
                _state.update { it.copy(avatarBytes = action.bytes) }
            }
            EditProfileAction.OnSaveClick -> {
                saveChanges()
            }
        }
    }

    private fun saveChanges() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, success = false) }

            val currentState = _state.value
            var currentAvatarUrl = currentState.avatarUrl

            // 1. Upload Avatar if changed
            currentState.avatarBytes?.let { bytes ->
                when (val result = firebaseAuthRepository.uploadProfileImage(bytes, currentState.email)) {
                    is Result.Success -> {
                        currentAvatarUrl = result.data
                    }
                    is Result.Error -> {
                        _state.update { it.copy(isLoading = false, error = result.message) }
                        return@launch
                    }
                    is Result.Loading -> { /* Handle if needed */ }
                }
            }

            // 2. Update User Data in Database
            val updatedUser = currentUser?.copy(
                displayName = currentState.fullName,
                email = currentState.email,
                profileUrl = currentAvatarUrl ?: ""
            ) ?: return@launch

            when (val result = firebaseAuthRepository.saveUserToDatabase(updatedUser)) {
                is Result.Success -> {
                    // 3. Update Password if provided
                    if (currentState.newPassword.isNotEmpty()) {
                        if (currentState.newPassword != currentState.confirmPassword) {
                            _state.update { it.copy(isLoading = false, error = "Passwords do not match") }
                            return@launch
                        }
                        
                        // We might need to re-authenticate or check current password here 
                        // but for now using resetPassword which updates it for the current user in repo
                        when (val pwResult = firebaseAuthRepository.resetPassword(currentState.email, currentState.newPassword)) {
                            is Result.Success -> {
                                _state.update { it.copy(isLoading = false, success = true, currentPassword = "", newPassword = "", confirmPassword = "") }
                                _event.emit(EditProfileEvent.ShowToast("Profile updated successfully"))
                            }
                            is Result.Error -> {
                                _state.update { it.copy(isLoading = false, error = pwResult.message) }
                            }
                            is Result.Loading -> { /* Handle if needed */ }
                        }
                    } else {
                        _state.update { it.copy(isLoading = false, success = true) }
                        _event.emit(EditProfileEvent.ShowToast("Profile updated successfully"))
                    }
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is Result.Loading -> { /* Handle if needed */ }
            }
        }
    }
}
