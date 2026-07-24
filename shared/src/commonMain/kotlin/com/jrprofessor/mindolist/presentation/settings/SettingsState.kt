package com.jrprofessor.mindolist.presentation.settings

import com.jrprofessor.mindolist.theme.ThemeMode

data class SettingsState(
    val isLoading: Boolean = false,
    val pendingAction: SettingsAction? = null,
    var showLogoutConfirmDialog: Boolean = false,
    val themeMode: ThemeMode = ThemeMode.DARK,
    val notificationsEnabled: Boolean = true,
    val aiExtractionEnabled: Boolean = true,
    val errorMessage: String? = null
)
