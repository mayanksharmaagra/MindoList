package com.jrprofessor.mindolist.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class GoogleItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val dateTime: LocalDateTime? = null,
    val type: GoogleItemType,
    val isCompleted: Boolean = false
)

@Serializable
enum class GoogleItemType {
    CALENDAR_EVENT,   // meetings
    CALENDAR_REMINDER,// reminders from calendar
    TASK              // google tasks
}
