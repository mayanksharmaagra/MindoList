package com.jrprofessor.mindolist.presentation.addTask

sealed class AddTaskEvent {
    data class Success(val message: String) : AddTaskEvent()
    data class Error(val error: String) : AddTaskEvent()
}