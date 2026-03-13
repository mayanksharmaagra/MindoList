package com.jrprofessor.mindolist.presentation

import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.model.TaskModel

data class DashboardState (
    // Tasks
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    // DateTime

    val user: User?=null,
    val currentDate:String="",
    val greeting: String=""
)