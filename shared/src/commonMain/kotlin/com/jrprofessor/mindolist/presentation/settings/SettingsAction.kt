package com.jrprofessor.mindolist.presentation.settings

import com.jrprofessor.mindolist.theme.ThemeMode

sealed class SettingsAction {
    data object Logout : SettingsAction()
    data object ShowProfile : SettingsAction()
    data object EditProfile : SettingsAction()
    data class SetThemeMode(val mode: ThemeMode) : SettingsAction()
    data class SetNotificationsEnabled(val enabled: Boolean) : SettingsAction()
    data class SetAiExtractionEnabled(val enabled: Boolean) : SettingsAction()
    data class SetDefaultView(val mode: com.jrprofessor.mindolist.presentation.settings.ViewMode) : SettingsAction()
}
