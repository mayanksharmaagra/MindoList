package com.jrprofessor.mindolist.presentation

import kotlinx.datetime.LocalDate

sealed class DashboardAction {
    data object LoadTasks : DashboardAction()
    data class DateByTask(val selectedDate: LocalDate) : DashboardAction()
    data object LoadUserData : DashboardAction()
    data object UpdateDateTime : DashboardAction()
    data class FilterByCategory(val category: String?) : DashboardAction()
    data class SelectedDate(val date: LocalDate) : DashboardAction()
    data class MarkComplete(val taskId: String, val isCompleted: Boolean) : DashboardAction()
    data class FilterSelected(val filterLabel: String) : DashboardAction()
    data class DeleteTask(val taskId: String) : DashboardAction()
}