package com.jrprofessor.mindolist.model

import kotlinx.serialization.Serializable

@Serializable
enum class TaskSource {
    MY_TASK,
    GOOGLE_TASK,
    GOOGLE_CALENDAR,
    GOOGLE_REMINDER
}

data class TaskUIModel(
    val id: String,
    val title: String,
    val description: String? = null,
    val dueDate: Long, // epoch millis
    val priority: String = Priority.MEDIUM.label,
    val category: String = Category.PERSONAL.label,
    val isCompleted: Boolean = false,
    val source: TaskSource = TaskSource.MY_TASK,
    val originalModel: Any? = null // Reference to TaskModel or GoogleItem
)
