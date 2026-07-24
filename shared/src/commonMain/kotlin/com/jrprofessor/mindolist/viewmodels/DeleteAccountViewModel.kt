package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DeleteAccountState(
    val confirmationText: String = "",
    val isConfirmed: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
) {
    val isDeleteEnabled = confirmationText.uppercase() == "DELETE" && isConfirmed && !isLoading
}

class DeleteAccountViewModel(
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DeleteAccountState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    fun onConfirmationTextChange(value: String) = _state.update { it.copy(confirmationText = value) }
    fun onConfirmedChange(value: Boolean) = _state.update { it.copy(isConfirmed = value) }

    fun deleteAccount() {
        if (!_state.value.isDeleteEnabled) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            when (val result = firebaseAuthRepository.deleteAccount()) {
                is Result.Success<*> -> {
                    _state.update { it.copy(isLoading = false, success = true) }
                    _event.emit("Account deleted successfully")
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message) }
                }
                is Result.Loading -> {}
            }
        }
    }
}
