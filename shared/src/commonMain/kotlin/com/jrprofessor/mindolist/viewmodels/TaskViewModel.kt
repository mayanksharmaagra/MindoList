package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.AiTaskRepository
import com.jrprofessor.mindolist.domain.usecase.AddTaskUseCase
import com.jrprofessor.mindolist.model.Category
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.presentation.addTask.AddTaskAction
import com.jrprofessor.mindolist.presentation.addTask.AddTaskEvent
import com.jrprofessor.mindolist.presentation.addTask.AddTaskEvent.Error
import com.jrprofessor.mindolist.presentation.addTask.AddTaskEvent.Success
import com.jrprofessor.mindolist.presentation.addTask.AddTaskUiState
import com.jrprofessor.mindolist.presentation.dashboard.DashboardEvent
import com.jrprofessor.mindolist.utils.NetworkConnectivityManager
import com.jrprofessor.mindolist.utils.SpeechToTextParser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.Clock

class TaskViewModel(
    private val addTaskUseCase: AddTaskUseCase,
    private val aiRepository: AiTaskRepository? = null,
    private val sttParser: SpeechToTextParser,
    private val networkConnectivityManager: NetworkConnectivityManager
) : ViewModel() {
    private val _state = MutableStateFlow(AddTaskUiState())
    val state: StateFlow<AddTaskUiState> = _state.asStateFlow()
    private val _eventTask = Channel<DashboardEvent>(Channel.BUFFERED)
    val eventTask = _eventTask.receiveAsFlow()

    private val _addTaskEffect = MutableSharedFlow<AddTaskEvent>()
    val addTaskEffect: SharedFlow<AddTaskEvent> = _addTaskEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            sttParser.state.collectLatest { sttState ->
                _state.update { 
                    it.copy(
                        isRecording = sttState.isSpeaking,
                        naturalInput = if (sttState.spokenText.isNotEmpty()) sttState.spokenText else it.naturalInput,
                        aiError = sttState.error
                    )
                }
            }
        }
    }

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
            is AddTaskAction.DurationChanged,
            is AddTaskAction.NaturalInputChanged,
            is AddTaskAction.ResetState -> _state.update {
                reduceTask(it, action)
            }

            AddTaskAction.SaveClicked -> saveTask()
            AddTaskAction.ParseAiClicked -> parseWithAi()
            AddTaskAction.ToggleRecording -> {
                if (_state.value.isRecording) {
                    sttParser.stopListening()
                } else {
                    sttParser.startListening()
                }
            }
        }
    }

    private fun parseWithAi() {
        val input = _state.value.naturalInput
        if (input.isBlank() || aiRepository == null) return

        viewModelScope.launch {
            if (!networkConnectivityManager.isNetworkAvailable()) {
                _state.update { it.copy(aiError = "No internet connection") }
                return@launch
            }
            _state.update { it.copy(isParsingAi = true, aiError = null) }
            try {
                val parsed = aiRepository.parseReminderText(input)
                _state.update {
                    it.copy(
                        title = parsed.title,
                        description = parsed.description,
                        category = Category.entries.find { cat -> cat.label.equals(parsed.category, ignoreCase = true) } ?: Category.PERSONAL,
                        selectedDate = parsed.date ?: it.selectedDate,
                        selectedTime = parsed.time ?: it.selectedTime,
                        isParsingAi = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(isParsingAi = false, aiError = e.message ?: "Couldn't parse. Try again.")
                }
            }
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
        is AddTaskAction.DurationChanged -> state.copy(duration = action.minutes)
        is AddTaskAction.NaturalInputChanged -> state.copy(naturalInput = action.value)
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
                reminderValue = st.reminderOption.label,
                duration = st.duration,
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
