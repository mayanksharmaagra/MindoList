package com.jrprofessor.mindolist.presentation.addTask

import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.screen.ReminderOption

sealed class AddTaskEvent {
    data class Success(val message: String) : AddTaskEvent()
    data class Error(val error: String) : AddTaskEvent()
}