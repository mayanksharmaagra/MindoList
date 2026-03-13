package com.jrprofessor.mindolist.model

import kotlin.time.Clock

data class TaskModel(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val dueDate: Long? = null,         // epoch millis
    val priority: String = Priority.MEDIUM.label,
    val category: String = Category.PERSONAL.label,
    val reminderEnabled: Boolean = false,
    val reminderValue: String = "",    // epoch millis
    val isCompleted: Boolean = false,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val updatedAt: Long = Clock.System.now().toEpochMilliseconds(),
)