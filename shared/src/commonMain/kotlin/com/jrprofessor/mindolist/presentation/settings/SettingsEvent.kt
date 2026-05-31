package com.jrprofessor.mindolist.presentation.settings

import com.jrprofessor.mindolist.model.TaskModel

sealed class SettingsEvent {
    data class Message(val message: String) : SettingsEvent()
    data object NavigateToSignUp : SettingsEvent()
}