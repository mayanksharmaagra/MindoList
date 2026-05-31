package com.jrprofessor.mindolist.presentation.dashboard

import com.jrprofessor.mindolist.model.TaskModel

sealed class DashboardEvent {
    data class Error(val message: String) : DashboardEvent()
    data class TaskCompleted(val task: TaskModel) : DashboardEvent()
    data class TaskDeleted(val task: TaskModel) : DashboardEvent()
}