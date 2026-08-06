package com.jrprofessor.mindolist.presentation.dashboard

import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.extension.today
import com.jrprofessor.mindolist.model.Filter
import com.jrprofessor.mindolist.model.TaskUIModel
import com.jrprofessor.mindolist.presentation.settings.ViewMode
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

data class DashboardState (
    // Tasks
    val tasks: List<TaskUIModel> = emptyList(),
    val todayTasks: List<TaskUIModel> = emptyList(),
    val totalTasksCount: Int = 0,
    val myTasksCount: Int = 0,
    val googleTasksCount: Int = 0,
    val allTasksCount: Int = 0,
    val isLoading: Boolean = true,
    val isGoogleSyncing: Boolean = false,
    val error: String? = null,

    // DateTime

    val user: User?=null,
    val currentDate:String="",
    val greeting: String="",
    val selectedDate: LocalDate = today(),
    val selectedFilter: String= Filter.ALL.name,
    val selectedSourceFilter: String = "ALL", // ALL, MY_TASKS, GOOGLE
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val viewMode: ViewMode = ViewMode.GROUPED
)
