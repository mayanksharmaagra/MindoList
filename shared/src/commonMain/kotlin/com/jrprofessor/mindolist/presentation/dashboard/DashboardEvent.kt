package com.jrprofessor.mindolist.presentation.dashboard

import com.jrprofessor.mindolist.model.TaskModel

sealed class DashboardEvent {
    data class Error(val message: String) : DashboardEvent()
    data object TaskCompleted : DashboardEvent()
    data object TaskDeleted : DashboardEvent()
    data object SyncSuccess : DashboardEvent()
}