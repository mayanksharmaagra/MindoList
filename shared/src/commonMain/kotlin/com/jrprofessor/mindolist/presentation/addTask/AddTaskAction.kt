package com.jrprofessor.mindolist.presentation.addTask

import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.screen.ReminderOption

sealed class AddTaskAction{
    // Pure state → reducer
    data class TitleChanged(val value: String) : AddTaskAction()
    data class DescriptionChanged(val value: String) : AddTaskAction()
    data class DateSelected(val date: String) : AddTaskAction()
    data class TimeSelected(val time: String) : AddTaskAction()
    data class PriorityChanged(val priority: Priority) : AddTaskAction()
    data class CategoryChanged(val category: Category) : AddTaskAction()
    data class ReminderToggled(val enabled: Boolean) : AddTaskAction()
    data class ReminderValue(val reminder: ReminderOption) : AddTaskAction()
    data object ResetState : AddTaskAction()

    // Side effects → ViewModel
    data object SaveClicked : AddTaskAction()

    // AI Actions
    data class NaturalInputChanged(val value: String) : AddTaskAction()
    data object ParseAiClicked : AddTaskAction()
    data object ToggleRecording : AddTaskAction()
}
