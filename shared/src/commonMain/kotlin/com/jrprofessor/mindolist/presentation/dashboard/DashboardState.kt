package com.jrprofessor.mindolist.presentation.dashboard

import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.extension.today
import com.jrprofessor.mindolist.model.Filter
import com.jrprofessor.mindolist.model.TaskModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class DashboardState (
    // Tasks
    val tasks: List<TaskModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    // DateTime

    val user: User?=null,
    val currentDate:String="",
    val greeting: String="",
    val selectedDate: LocalDate = today(),
    val selectedFilter: String= Filter.ALL.name
)