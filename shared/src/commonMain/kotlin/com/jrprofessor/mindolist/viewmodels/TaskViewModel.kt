package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.usecase.AddTaskUseCase
import com.jrprofessor.mindolist.domain.usecase.GetTasksUseCase
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.presentation.AddTaskAction
import com.jrprofessor.mindolist.presentation.AddTaskEvent
import com.jrprofessor.mindolist.presentation.AddTaskEvent.*
import com.jrprofessor.mindolist.presentation.AddTaskUiState
import com.jrprofessor.mindolist.presentation.DashboardEvent
import com.jrprofessor.mindolist.presentation.DashboardState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Clock

class TaskViewModel(
    private val addTaskUseCase: AddTaskUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(AddTaskUiState())
    val state: StateFlow<AddTaskUiState> = _state.asStateFlow()
    private val _eventTask = Channel<DashboardEvent>(Channel.BUFFERED)
    val eventTask = _eventTask.receiveAsFlow()

    private val _addTaskEffect = MutableSharedFlow<AddTaskEvent>()
    val addTaskEffect: SharedFlow<AddTaskEvent> = _addTaskEffect.asSharedFlow()

    fun dispatch(action: AddTaskAction) {
        when (action) {
            is AddTaskAction.CategoryChanged,
            is AddTaskAction.DateSelected,
            is AddTaskAction.DescriptionChanged,
            is AddTaskAction.PriorityChanged,
            is AddTaskAction.ReminderToggled,
            is AddTaskAction.TimeSelected,
            is AddTaskAction.TitleChanged,
            is AddTaskAction.ReminderValue,
            is AddTaskAction.ResetState -> _state.update {
                reduceTask(it, action)
            }

            AddTaskAction.SaveClicked -> saveTask()
        }
    }

    private fun reduceTask(
        state: AddTaskUiState,
        action: AddTaskAction
    ): AddTaskUiState = when (action) {
        is AddTaskAction.TitleChanged -> state.copy(title = action.value, titleError = null)
        is AddTaskAction.DescriptionChanged -> state.copy(description = action.value)
        is AddTaskAction.DateSelected -> state.copy(selectedDate = action.date, dateError = null)
        is AddTaskAction.TimeSelected -> state.copy(selectedTime = action.time)
        is AddTaskAction.PriorityChanged -> state.copy(priority = action.priority)
        is AddTaskAction.CategoryChanged -> state.copy(category = action.category)
        is AddTaskAction.ReminderToggled -> state.copy(reminderEnabled = action.enabled)
        is AddTaskAction.ReminderValue -> state.copy(reminderOption = action.reminder)
        AddTaskAction.ResetState -> AddTaskUiState()
        else -> state
    }

    private fun saveTask() {
        if (!validate()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val st = _state.value
            val task = TaskModel(
                id = if (st.isEditMode) st.editTaskId else "",
                title = st.title.trim(),
                description = st.description.trim(),
                dueDate = buildDueDateMillis(st.selectedDate, st.selectedTime),
                priority = st.priority.label,
                category = st.category.label,
                reminderEnabled = st.reminderEnabled,
                reminderValue =st.reminderOption.label ,
                updatedAt = Clock.System.now().toEpochMilliseconds(),
            )

            val result = addTaskUseCase(task)

            _state.update { it.copy(isLoading = false) }

            when (result) {
                is Result.Success -> {
                    _addTaskEffect.emit(
                        Success(
                            if (st.isEditMode) "Task updated successfully ✓"
                            else "Task added successfully ✓"
                        )
                    )
                    _state.update { reduceTask(it, AddTaskAction.ResetState) }
                }

                is Result.Error -> _addTaskEffect.emit(
                    Error(
                        result.message ?: "Failed to save task"
                    )
                )

                is Result.Loading -> Unit
            }
        }
    }

    fun buildDueDateMillis(selectedDate: String, selectedTime: String): Long {
        if (selectedDate.isBlank() || selectedTime.isBlank()) return 0L
        return runCatching {
            // "12/03/2025" → day, month, year
            val (day, month, year) = selectedDate.split("/").map { it.toInt() }

            // "11:30 PM" → hour, minute, period
            val (timePart, period) = selectedTime.trim().split(" ")
            val (rawHour, minute) = timePart.split(":").map { it.toInt() }

            // 12-hour → 24-hour convert
            val hour24 = when {
                period.uppercase() == "AM" && rawHour == 12 -> 0       // 12 AM = 00:00
                period.uppercase() == "PM" && rawHour != 12 -> rawHour + 12  // PM = +12
                else -> rawHour
            }

            LocalDateTime(
                year = year,
                monthNumber = month,    // ← month → monthNumber
                dayOfMonth = day,       // ← day → dayOfMonth
                hour = hour24,
                minute = minute,
                second = 0,
                nanosecond = 0,
            ).toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds()

        }.getOrDefault(0L)
    }

    private fun validate(): Boolean {
        var isValid = true
        if (_state.value.title.isBlank()) {
            _state.update { it.copy(titleError = "Title cannot be empty") }
            isValid = false
        }
        return isValid
    }
}