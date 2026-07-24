package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.domain.usecase.GetTasksUseCase
import com.jrprofessor.mindolist.model.Filter
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardEvent
import com.jrprofessor.mindolist.presentation.dashboard.DashboardState
import com.jrprofessor.mindolist.utils.Logger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
        startDateTimeTimer()
    }
    
    private fun startDateTimeTimer() {
        viewModelScope.launch {
            while (true) {
                delay(60000) // Update every minute
                updateDateTime()
            }
        }
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
                applyFilter(_state.value.selectedFilter, _state.value.searchQuery)
            }

            is DashboardAction.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
                applyFilter(_state.value.selectedFilter, action.query)
            }

            is DashboardAction.ToggleSearch -> {
                _state.update { 
                    val newActive = !it.isSearchActive
                    it.copy(
                        isSearchActive = newActive,
                        searchQuery = if (!newActive) "" else it.searchQuery
                    )
                }
                if (!_state.value.isSearchActive) {
                    applyFilter(_state.value.selectedFilter, "")
                }
            }
        }
    }
    private fun applyFilter(filterLabel: String, query: String = "") {
        val filter = Filter.entries.find { it.name == filterLabel } ?: Filter.ALL
        
        var filtered = when (filter) {
            Filter.ALL       -> _allTasks.value
            Filter.PENDING   -> _allTasks.value.filter { !it.isCompleted }
            Filter.COMPLETED -> _allTasks.value.filter { it.isCompleted }
            Filter.OVERDUE   -> _allTasks.value.filter {
                !it.isCompleted && it.dueDate < Clock.System.now().toEpochMilliseconds()
            }
        }

        if (query.isNotBlank()) {
            filtered = filtered.filter { 
                it.title.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true)
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
                    // Apply current filter instead of resetting to _allTasks
                    applyFilter(_state.value.selectedFilter, _state.value.searchQuery)
                    _state.update { it.copy(isLoading = false) }
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
                        Logger.error {
                            "Loading tasks :${result.data}"
                        }
                        _allTasks.value = result.data as List<TaskModel>
                        applyFilter(_state.value.selectedFilter, _state.value.searchQuery)
                        _state.update {
                            it.copy(
                                isLoading = false,
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

    fun logoutUser() {

    }
}