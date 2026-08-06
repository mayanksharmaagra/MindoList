package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.repository.GoogleCalendarRepository
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.domain.usecase.GetTasksUseCase
import com.jrprofessor.mindolist.model.Filter
import com.jrprofessor.mindolist.model.GoogleItem
import com.jrprofessor.mindolist.model.GoogleItemType
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.model.TaskSource
import com.jrprofessor.mindolist.model.TaskUIModel
import com.jrprofessor.mindolist.presentation.settings.ViewMode
import com.jrprofessor.mindolist.presentation.dashboard.DashboardAction
import com.jrprofessor.mindolist.presentation.dashboard.DashboardEvent
import com.jrprofessor.mindolist.presentation.dashboard.DashboardState
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import com.jrprofessor.mindolist.utils.Logger
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class DashboardViewModel(
    val getTaskUseCase: GetTasksUseCase,
    val firebaseAuthRepository: FirebaseAuthRepository,
    val taskRepository: TaskRepository,
    val googleCalendarRepository: GoogleCalendarRepository,
    val googleAuthManager: GoogleAuthManager,
    private val appSettings: com.jrprofessor.mindolist.local.AppSettings
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    private val _event = Channel<DashboardEvent>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()
    private val _allTasks = MutableStateFlow<List<TaskUIModel>>(emptyList())
    private val _myTasks = MutableStateFlow<List<TaskUIModel>>(emptyList())
    private val _googleItems = MutableStateFlow<List<TaskUIModel>>(emptyList())

    private var isGoogleLoading = false
    private var isFirebaseLoading = false
    private var isAuthObserved = false
    private var searchJob: Job? = null
    private var googleSyncJob: Job? = null

    init {
        _state.update { it.copy(isLoading = true) }
        dispatch(DashboardAction.LoadUserData)
        dispatch(DashboardAction.UpdateDateTime)
        startDateTimeTimer()
        observeGoogleAuth()
        observeViewMode()
    }

    private fun observeViewMode() {
        viewModelScope.launch {
            appSettings.defaultViewFlow.collect { modeStr ->
                runCatching {
                    ViewMode.valueOf(modeStr)
                }.getOrNull()?.let { mode ->
                    _state.update { it.copy(viewMode = mode) }
                }
            }
        }
    }

    private fun observeGoogleAuth() {
        viewModelScope.launch {
            firebaseAuthRepository.getCurrentUser()
                .map { it?.googleAccessToken }
                .distinctUntilChanged()
                .collectLatest { token ->
                    isAuthObserved = true
                    if (token != null) {
                        isGoogleLoading = true
                        val refreshedToken = googleAuthManager.refreshAccessToken()
                        fetchGoogleItems(refreshedToken ?: token)
                    } else {
                        isGoogleLoading = false
                        _googleItems.value = emptyList()
                        combineAllTasks()
                    }
                }
        }
    }

    private fun fetchGoogleItems(accessToken: String) {
        googleSyncJob?.cancel()
        googleSyncJob = viewModelScope.launch {
            try {
                isGoogleLoading = true
                _state.update { it.copy(isGoogleSyncing = true) }
                val items: List<GoogleItem> = googleCalendarRepository.fetchAll(accessToken)
                Logger.debug { "Google Task Response: ${items.size} items" }
                _googleItems.value = items.map { it.toTaskUIModel() }
            } catch (e: Exception) {
                Logger.error { "Failed to fetch Google items: ${e.message}" }
                _event.send(DashboardEvent.Error("Google Sync: ${e.message}"))
            } finally {
                isGoogleLoading = false
                _state.update { it.copy(isGoogleSyncing = false) }
                combineAllTasks()
            }
        }
    }

    private fun combineAllTasks() {
        val selectedDate = _state.value.selectedDate
        
        // All tasks logic
        val allCombined = (_myTasks.value + _googleItems.value).sortedBy { it.dueDate }
        _allTasks.value = allCombined
        
        // Dashboard (Today's) logic
        val todayCombined = allCombined.filter { task ->
            if (task.dueDate == 0L) return@filter false
            val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(task.dueDate)
            val taskDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            taskDate == selectedDate
        }

        updateCounts(allCombined)
        applyFilter(_state.value.selectedFilter, _state.value.selectedSourceFilter, _state.value.searchQuery)
        
        _state.update { it.copy(todayTasks = todayCombined) }

        if (isAuthObserved && !isGoogleLoading && !isFirebaseLoading) {
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun updateCounts(allTasks: List<TaskUIModel>) {
        val myTasksCount = allTasks.count { it.source == TaskSource.MY_TASK }
        val googleTasksCount = allTasks.count { it.source != TaskSource.MY_TASK }
        _state.update {
            it.copy(
                allTasksCount = allTasks.size,
                myTasksCount = myTasksCount,
                googleTasksCount = googleTasksCount,
                totalTasksCount = allTasks.size
            )
        }
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
            is DashboardAction.DateByTask -> {
                _state.update { it.copy(selectedDate = action.selectedDate) }
                loadTasks(action.selectedDate)
            }
            is DashboardAction.LoadUserData -> loadUserData()
            is DashboardAction.UpdateDateTime -> updateDateTime()
            is DashboardAction.FilterByCategory -> {

            }
            is DashboardAction.MarkComplete -> {
                markAsCompleted(action.taskId, action.isCompleted)
            }
            is DashboardAction.DeleteTask -> {
                deleteTask(action.taskId)
            }

            is DashboardAction.SelectedDate -> {
                _state.update { it.copy(selectedDate = action.date) } // ← state update
                loadTasks(action.date)
            }

            is DashboardAction.FilterSelected -> {
                _state.update { it.copy(selectedFilter = action.filterLabel) }
                applyFilter(_state.value.selectedFilter, _state.value.selectedSourceFilter, _state.value.searchQuery)
            }

            is DashboardAction.SourceFilterSelected -> {
                _state.update { it.copy(selectedSourceFilter = action.sourceLabel) }
                applyFilter(_state.value.selectedFilter, action.sourceLabel, _state.value.searchQuery)
            }

            is DashboardAction.SearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(300) // Debounce for 300ms
                    applyFilter(_state.value.selectedFilter, _state.value.selectedSourceFilter, action.query)
                }
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
                    applyFilter(_state.value.selectedFilter, _state.value.selectedSourceFilter, "")
                }
            }
            is DashboardAction.RefreshGoogleTasks -> {
                refreshGoogleItems()
            }
        }
    }

    private fun refreshGoogleItems() {
        viewModelScope.launch {
            val user = firebaseAuthRepository.getCurrentUser().first()
            val token = user?.googleAccessToken
            if (token != null) {
                isGoogleLoading = true
                _state.update { it.copy(isLoading = true) }
                try {
                    val refreshedToken = googleAuthManager.refreshAccessToken()
                    fetchGoogleItems(refreshedToken ?: token)
                    _event.send(DashboardEvent.Error("Sync completed ✓")) // Using Error event for success msg as shortcut or add Message event
                } catch (e: Exception) {
                    isGoogleLoading = false
                    _state.update { it.copy(isLoading = false) }
                    _event.send(DashboardEvent.Error("Sync failed: ${e.message}"))
                }
            } else {
                _event.send(DashboardEvent.Error("Google account not connected"))
            }
        }
    }
    private fun applyFilter(filterLabel: String, sourceLabel: String = "ALL", query: String = "") {
        val filter = Filter.entries.find { it.name == filterLabel } ?: Filter.ALL
        val now = Clock.System.now().toEpochMilliseconds()
        
        val statusFiltered = when (filter) {
            Filter.ALL -> _allTasks.value.filter {
                it.isCompleted || it.dueDate == 0L || it.dueDate >= now
            }
            Filter.PENDING -> _allTasks.value.filter {
                !it.isCompleted && (it.dueDate == 0L || it.dueDate >= now)
            }
            Filter.COMPLETED -> _allTasks.value.filter {
                it.isCompleted
            }
            Filter.OVERDUE -> _allTasks.value.filter {
                !it.isCompleted && it.dueDate != 0L && it.dueDate < now
            }
        }

        // Update counts based on the status filter
        val myCount = statusFiltered.count { it.source == TaskSource.MY_TASK }
        val googleCount = statusFiltered.count { it.source != TaskSource.MY_TASK }
        
        _state.update {
            it.copy(
                allTasksCount = statusFiltered.size,
                myTasksCount = myCount,
                googleTasksCount = googleCount
            )
        }

        var finalFiltered = when (sourceLabel) {
            "MY_TASKS" -> statusFiltered.filter { it.source == TaskSource.MY_TASK }
            "GOOGLE" -> statusFiltered.filter { it.source != TaskSource.MY_TASK }
            else -> statusFiltered
        }

        if (query.isNotBlank()) {
            finalFiltered = finalFiltered.filter { 
                it.title.contains(query, ignoreCase = true) || 
                (it.description?.contains(query, ignoreCase = true) ?: false)
            }
        }
        
        _state.update { it.copy(tasks = finalFiltered) }
    }

    private fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = taskRepository.deleteTask(taskId)) {
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    _event.send(DashboardEvent.Error(result.message ?: "Failed to delete task"))
                }
                Result.Loading -> Unit
                is Result.Success<*> -> {
                    _myTasks.update { tasks -> tasks.filter { it.id != taskId } }
                    combineAllTasks()
                    _event.send(DashboardEvent.TaskDeleted)
                }
            }
        }
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
                    _myTasks.update { tasks ->
                        tasks.map { task ->
                            if (task.id == taskId) task.copy(isCompleted = completed)
                            else task
                        }
                    }
                    combineAllTasks()
                    _event.send(DashboardEvent.TaskCompleted)
                }
            }
        }
    }

    private fun loadTasks(selectedDate: LocalDate?) {
        viewModelScope.launch {
            Logger.debug { "Loading tasks :$selectedDate" }
            isFirebaseLoading = true
            _state.update { it.copy(isLoading = true) }
            getTaskUseCase(selectedDate).collect { result ->
                when(result){
                    is Result.Error -> {
                        isFirebaseLoading = false
                        _event.send(DashboardEvent.Error(result.message?:"Something went wrong"))
                        combineAllTasks()
                    }
                    Result.Loading -> Unit
                    is Result.Success<*> -> {
                        Logger.error {
                            "Loading tasks :${result.data}"
                        }
                        val taskList: List<TaskUIModel> = (result.data as List<TaskModel>).map { it.toTaskUIModel() }
                        _myTasks.value = taskList
                        isFirebaseLoading = false
                        combineAllTasks()
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

fun TaskModel.toTaskUIModel() = TaskUIModel(
    id = id,
    title = title,
    description = description,
    dueDate = dueDate,
    duration = duration,
    priority = priority,
    category = category,
    isCompleted = isCompleted,
    source = TaskSource.MY_TASK,
    originalModel = this
)

fun GoogleItem.toTaskUIModel(): TaskUIModel {
    return TaskUIModel(
        id = id,
        title = title,
        description = description,
        dueDate = dateTime?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds() ?: 0L, 
        isCompleted = isCompleted,
        source = when (type) {
            GoogleItemType.TASK -> TaskSource.GOOGLE_TASK
            GoogleItemType.CALENDAR_EVENT -> TaskSource.GOOGLE_CALENDAR
            GoogleItemType.CALENDAR_REMINDER -> TaskSource.GOOGLE_REMINDER
        },
        originalModel = this
    )
}
