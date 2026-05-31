package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.presentation.settings.SettingsAction
import com.jrprofessor.mindolist.presentation.settings.SettingsEvent
import com.jrprofessor.mindolist.presentation.settings.SettingsState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewmodel(
    val firebaseAuthRepository: FirebaseAuthRepository,
) : ViewModel() {

    private val _event = Channel<SettingsEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()


    fun dispatch(action: SettingsAction) {
        when (action) {
            is SettingsAction.Logout -> _state.update { it.copy(showLogoutConfirmDialog = true) }
            is SettingsAction.ShowProfile -> _state.update { it.copy(pendingAction = action) }
            is SettingsAction.EditProfile -> _state.update { it.copy(pendingAction = action) }
        }
    }


    fun confirmLogout() {
        _state.update { it.copy(showLogoutConfirmDialog = false) }
        logoutUser()
    }

    fun dismissLogoutDialog() {
        _state.update { it.copy(showLogoutConfirmDialog = false) }
    }

    private fun logoutUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }  // Show loader

            when (val result = firebaseAuthRepository.signOut()) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(SettingsEvent.NavigateToSignUp)  // Navigate after success
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }  // Hide loader on error
                    _event.send(SettingsEvent.Message(result.message ?: "Something went wrong"))
                }
                Result.Loading -> Unit
            }
        }
    }
}