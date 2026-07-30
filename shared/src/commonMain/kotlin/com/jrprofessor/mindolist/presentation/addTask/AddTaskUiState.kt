package com.jrprofessor.mindolist.presentation.addTask

import com.jrprofessor.mindolist.extension.toDateFormat
import com.jrprofessor.mindolist.extension.toTimeFormat
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.Priority
import com.jrprofessor.mindolist.screen.ReminderOption
import kotlin.time.Clock
import kotlin.time.Duration.Companion.milliseconds

data class AddTaskUiState(
    var title: String = "",
    var description: String = "",
    var selectedDate: String = Clock.System.now().toEpochMilliseconds().milliseconds.toDateFormat(),
    var selectedTime: String = Clock.System.now().toEpochMilliseconds().toTimeFormat(),
    var priority: Priority = Priority.MEDIUM,
    var category: Category = Category.PERSONAL,
    var reminderEnabled: Boolean = false,
    var reminderOption: ReminderOption = ReminderOption.FIFTEEN_MINUTES,
    var duration: Int = 0,
    var isLoading: Boolean = false,
    var isImageLoading: Boolean = false,
    var titleError: String? = null,
    var dateError: String? = null,
    var isEditMode: Boolean = false,
    var editTaskId: String = "",

    // AI related
    val naturalInput: String = "",
    val isParsingAi: Boolean = false,
    val aiError: String? = null,
    val isRecording: Boolean = false
)
