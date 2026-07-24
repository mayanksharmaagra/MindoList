package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.presentation.settings.SettingsAction
import com.jrprofessor.mindolist.presentation.settings.SettingsEvent
import com.jrprofessor.mindolist.presentation.settings.SettingsState
import com.jrprofessor.mindolist.theme.ThemeMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewmodel(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val appSettings: AppSettings,
) : ViewModel() {

    private val _event = Channel<SettingsEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        observeSettings()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            appSettings.themeModeFlow.collect { themeModeStr ->
                runCatching {
                    ThemeMode.valueOf(themeModeStr)
                }.getOrNull()?.let { themeMode ->
                    _state.update { it.copy(themeMode = themeMode) }
                }
            }
        }
        viewModelScope.launch {
            appSettings.notificationsFlow.collect { enabled ->
                _state.update { it.copy(notificationsEnabled = enabled) }
            }
        }
        viewModelScope.launch {
            appSettings.aiExtractionFlow.collect { enabled ->
                _state.update { it.copy(aiExtractionEnabled = enabled) }
            }
        }
    }

    fun dispatch(action: SettingsAction) {
        when (action) {
            is SettingsAction.Logout -> _state.update { it.copy(showLogoutConfirmDialog = true) }
            is SettingsAction.ShowProfile -> _state.update { it.copy(pendingAction = action) }
            is SettingsAction.EditProfile -> _state.update { it.copy(pendingAction = action) }
            is SettingsAction.SetThemeMode -> setThemeMode(action.mode)
            is SettingsAction.SetNotificationsEnabled -> {
                appSettings.notificationsEnabled = action.enabled
            }
            is SettingsAction.SetAiExtractionEnabled -> {
                appSettings.aiExtractionEnabled = action.enabled
            }
        }
    }

    private fun setThemeMode(mode: ThemeMode) {
        appSettings.themeMode = mode.name
        // _state update automatically through flow collection in observeSettings()
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
            _state.update { it.copy(isLoading = true) }

            when (val result = firebaseAuthRepository.signOut()) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(SettingsEvent.NavigateToSignUp)
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(SettingsEvent.Message(result.message ?: "Something went wrong"))
                }
                Result.Loading -> Unit
            }
        }
    }
}
