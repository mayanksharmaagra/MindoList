package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.presentation.settings.SettingsAction
import com.jrprofessor.mindolist.presentation.settings.SettingsEvent
import com.jrprofessor.mindolist.presentation.settings.SettingsState
import com.jrprofessor.mindolist.presentation.settings.ViewMode
import com.jrprofessor.mindolist.theme.ThemeMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SettingsViewmodel(
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val taskRepository: TaskRepository,
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
        viewModelScope.launch {
            appSettings.defaultViewFlow.collect { viewModeStr ->
                runCatching {
                    ViewMode.valueOf(viewModeStr)
                }.getOrNull()?.let { viewMode ->
                    _state.update { it.copy(defaultView = viewMode) }
                }
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
            is SettingsAction.SetDefaultView -> {
                appSettings.defaultView = action.mode.name
            }
            is SettingsAction.ExportTasks -> exportTasks()
        }
    }

    private fun exportTasks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // Fetch tasks (using first() to get the current list from the flow)
                val result = taskRepository.getTasks().first { it !is Result.Loading }
                if (result is Result.Success) {
                    val json = Json { prettyPrint = true }.encodeToString(result.data)
                    _event.send(SettingsEvent.ExportSuccess(json))
                    _event.send(SettingsEvent.Message("Tasks exported successfully!"))
                } else if (result is Result.Error) {
                    _event.send(SettingsEvent.Message(result.message ?: "Failed to fetch tasks"))
                }
            } catch (e: Exception) {
                _event.send(SettingsEvent.Message("Export failed: ${e.message}"))
            } finally {
                _state.update { it.copy(isLoading = false) }
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
