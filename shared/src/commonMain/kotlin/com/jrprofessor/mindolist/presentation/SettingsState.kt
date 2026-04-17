package com.jrprofessor.mindolist.presentation


data class SettingsState(
    val isLoading: Boolean = false,
    val pendingAction: SettingsAction? = null,
    var showLogoutConfirmDialog: Boolean = false,
    val errorMessage: String? = null
)