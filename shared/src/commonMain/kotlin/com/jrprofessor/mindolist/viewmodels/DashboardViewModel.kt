package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.domain.usecase.GetTasksUseCase
import com.jrprofessor.mindolist.model.Filter
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.presentation.DashboardAction
import com.jrprofessor.mindolist.presentation.DashboardEvent
import com.jrprofessor.mindolist.presentation.DashboardState
import com.jrprofessor.mindolist.utils.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
class DashboardViewModel(
    val getTaskUseCase: GetTasksUseCase,
    val firebaseAuthRepository: FirebaseAuthRepository,
    val taskRepository: TaskRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _event = Channel<DashboardEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()
    private val _allTasks = MutableStateFlow<List<TaskModel>>(emptyList())
    init {
        dispatch(DashboardAction.LoadUserData)
        dispatch(DashboardAction.UpdateDateTime)
    }
    fun dispatch(action: DashboardAction) {
        when (action) {
            is DashboardAction.LoadTasks -> loadTasks(null)
            is DashboardAction.DateByTask -> loadTasks(action.selectedDate)
            is DashboardAction.LoadUserData -> loadUserData()
            is DashboardAction.UpdateDateTime -> updateDateTime()
            is DashboardAction.FilterByCategory -> {

            }
            is DashboardAction.MarkComplete -> {
                markAsCompleted(action.taskId, action.isCompleted)
            }
            is DashboardAction.DeleteTask -> {

            }

            is DashboardAction.SelectedDate -> {
                _state.update { it.copy(selectedDate = action.date) } // ← state update
                loadTasks(action.date)
            }

            is DashboardAction.FilterSelected -> {
                _state.update { it.copy(selectedFilter = action.filterLabel) }
                applyFilter(action.filterLabel)
            }
        }
    }
    private fun applyFilter(filterLabel: String) {
        val filter = Filter.entries.find { it.name == filterLabel } ?: Filter.ALL
        val filtered = when (filter) {
            Filter.ALL       -> _allTasks.value
            Filter.PENDING   -> _allTasks.value.filter { !it.isCompleted }
            Filter.COMPLETED -> _allTasks.value.filter { it.isCompleted }
            Filter.OVERDUE   -> _allTasks.value.filter {
                !it.isCompleted && it.dueDate < Clock.System.now().toEpochMilliseconds()
            }
        }
        _state.update { it.copy(tasks = filtered) }
    }

    private fun markAsCompleted(taskId: String, completed: Boolean) {
        viewModelScope.launch {
            when (val result = taskRepository.markComplete(taskId, completed)) {
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(DashboardEvent.Error(result.message ?: "Something went wrong"))
                }

                Result.Loading -> Unit
                is Result.Success<*> -> {

                    // Local state update karo — Firebase realtime se bhi update aayega
                    _allTasks.update { tasks ->
                        tasks.map { task ->
                            if (task.id == taskId) task.copy(isCompleted = completed)
                            else task
                        }
                    }
                    _state.update { it.copy(tasks = _allTasks.value, isLoading = false) }
                }
            }
        }
    }

    private fun loadTasks(selectedDate: LocalDate?) {
        viewModelScope.launch {
            Logger.debug { "Loading tasks :$selectedDate" }
            getTaskUseCase(selectedDate).collect { result ->
                when(result){
                    is Result.Error -> {
                        _state.update { it.copy(isLoading = false) }
                        _event.send(DashboardEvent.Error(result.message?:"Something went wrong"))
                    }
                    Result.Loading -> Unit
                    is Result.Success<*> -> {
                        _allTasks.value = result.data as List<TaskModel>
                        _state.update {
                            it.copy(
                                isLoading = false,
                                tasks = _allTasks.value,
                                error = null,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            firebaseAuthRepository.getCurrentUser().collectLatest { user ->
                _state.update {
                    it.copy(
                        user = user,
                        greeting = getGreeting(),
                        currentDate = getCurrentDate(),
                        isLoading = false
                    )
                }
            }
        }
    }
    private fun updateDateTime() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    currentDate = getCurrentDate(),
                    greeting = getGreeting()
                )
            }
        }
    }
    @OptIn(ExperimentalTime::class)
    private fun getCurrentDate(): String {
        val now = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val day = now.dayOfWeek.name.take(3)          // MON, TUE...
            .lowercase().replaceFirstChar { it.uppercase() }
        val month = now.month.name.take(3)             // JAN, FEB...
            .lowercase().replaceFirstChar { it.uppercase() }
        val date = now.day.toString().padStart(2, '0')
        val year = now.year

        return "$day, $month, $date $year"             // Mon, Jan, 01 2025
    }

    @OptIn(ExperimentalTime::class)
    private fun getGreeting(): String {
        val hour = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .hour

        return when (hour) {
            in 0..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }
}