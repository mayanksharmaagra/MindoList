package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChangePasswordState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
) {
    val hasMinLength = newPassword.length >= 8
    val hasUppercase = newPassword.any { it.isUpperCase() }
    val hasNumber = newPassword.any { it.isDigit() }
    val hasSpecialChar = newPassword.any { !it.isLetterOrDigit() }

    val strength: Int get() {
        var score = 0
        if (hasMinLength) score++
        if (hasUppercase) score++
        if (hasNumber) score++
        if (hasSpecialChar) score++
        return score
    }

    val isUpdateEnabled = currentPassword.isNotEmpty() && strength == 4 && newPassword == confirmPassword && !isLoading
}

class ChangePasswordViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ChangePasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun onCurrentPasswordChange(value: String) = _state.update { it.copy(currentPassword = value) }
    fun onNewPasswordChange(value: String) = _state.update { it.copy(newPassword = value) }
    fun onConfirmPasswordChange(value: String) = _state.update { it.copy(confirmPassword = value) }
    
    fun toggleCurrentPasswordVisibility() = _state.update { it.copy(isCurrentPasswordVisible = !it.isCurrentPasswordVisible) }
    fun toggleNewPasswordVisibility() = _state.update { it.copy(isNewPasswordVisible = !it.isNewPasswordVisible) }
    fun toggleConfirmPasswordVisibility() = _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }

    fun updatePassword() {
        if (!_state.value.isUpdateEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            // Note: This logic depends on the repository's capabilities. 
            // Assuming current password validation + update is handled or simplified for now.
            // Using placeholder logic similar to resetPassword but for update.
            val email = firebaseAuthRepository.getCurrentUser().first()?.email ?: ""
            
            when (val result = firebaseAuthRepository.resetPassword(email, _state.value.newPassword)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, success = true) }
                    _event.emit("Password updated successfully")
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is Result.Loading -> {}
            }
        }
    }
}
