package com.jrprofessor.mindolist.presentation.settings

import kotlinx.datetime.LocalDate

sealed class SettingsAction {
    data object Logout : SettingsAction()
    data object ShowProfile : SettingsAction()
    data object EditProfile : SettingsAction()
}