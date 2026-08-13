package com.jrprofessor.mindolist.model

import kotlinx.serialization.Serializable
import kotlin.time.Clock

@Serializable
data class TaskModel(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: Long = 0L,         // epoch millis
    val priority: String = Priority.MEDIUM.label,
    val category: String = Category.PERSONAL.label,
    val reminderEnabled: Boolean = false,
    val reminderValue: String = "",    // epoch millis
    val duration: Int = 0,             // minutes
    var isCompleted: Boolean = false,
    val isPinned: Boolean = false,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    var updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)