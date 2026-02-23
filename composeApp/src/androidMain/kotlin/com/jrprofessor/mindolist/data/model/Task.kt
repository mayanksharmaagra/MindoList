package com.jrprofessor.mindolist.data.model

data class Task(
    val id: Long,
    val title: String,
    val time: String,
    val priority: Priority,
    val isCompleted: Boolean = false,
)