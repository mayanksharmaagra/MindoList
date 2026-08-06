package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.model.TaskModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock

enum class AnalyticsFilter { WEEK, MONTH, YEAR }

data class AnalyticsState(
    val selectedFilter: AnalyticsFilter = AnalyticsFilter.MONTH,
    val selectedDate: LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val completionPercentage: Int = 0,
    val totalTasks: Int = 0,
    val longestStreak: Int = 0,
    val chartData: List<ChartDataPoint> = emptyList(),
    val categoryProgress: List<CategoryStats> = emptyList(),
    val isLoading: Boolean = false,
    val rangeLabel: String = ""
)

data class ChartDataPoint(
    val label: String,
    val value: Float,
    val isHighlighted: Boolean = false
)

data class CategoryStats(
    val name: String,
    val progress: Float,
    val count: Int
)

class AnalyticsViewModel(
    private val taskRepository: TaskRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state.asStateFlow()

    init {
        loadAnalytics()
        observeUserStats()
    }

    private fun observeUserStats() {
        viewModelScope.launch {
            firebaseAuthRepository.getCurrentUser().collectLatest { user ->
                _state.update { 
                    it.copy(longestStreak = user?.currentStreak ?: 0) 
                }
            }
        }
    }

    fun setFilter(filter: AnalyticsFilter) {
        _state.update { it.copy(selectedFilter = filter) }
        loadAnalytics()
    }

    fun setDate(date: LocalDate) {
        _state.update { it.copy(selectedDate = date) }
        loadAnalytics()
    }

    private fun loadAnalytics() {
        val (start, end) = calculateRange(_state.value.selectedFilter, _state.value.selectedDate)
        
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            taskRepository.getTasksInRange(start, end).collect { result ->
                if (result is Result.Success) {
                    processTasks(result.data)
                }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun processTasks(tasks: List<TaskModel>) {
        val st = _state.value
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        
        // Filter out tasks that aren't relevant for this range's completion rate
        // Usually, we want to know: of all tasks that WERE due or completed in this range, how many are done?
        val relevantTasks = tasks.filter { 
            it.isCompleted || (it.dueDate != 0L && it.dueDate < now) || it.isSameDay(st.selectedDate)
        }

        val total = relevantTasks.size
        val completed = relevantTasks.count { it.isCompleted }
        val percentage = if (total > 0) (completed * 100) / total else 0

        val categoryStats = tasks.groupBy { it.category }
            .map { (categoryLabel, catTasks) ->
                val catTotal = catTasks.size
                val catCompleted = catTasks.count { it.isCompleted }
                val category = com.jrprofessor.mindolist.model.Category.entries.find { it.label.equals(categoryLabel, ignoreCase = true) } 
                    ?: com.jrprofessor.mindolist.model.Category.PERSONAL
                
                CategoryStats(
                    name = category.label,
                    progress = if (catTotal > 0) catCompleted.toFloat() / catTotal else 0f,
                    count = catTotal
                )
            }.sortedByDescending { it.progress }

        val chartData = aggregateChartData(tasks, st.selectedFilter, st.selectedDate)
        val streak = calculateStreak(tasks) // Simplified for the range

        _state.update {
            it.copy(
                totalTasks = total,
                completionPercentage = percentage,
                categoryProgress = categoryStats,
                longestStreak = streak,
                chartData = chartData,
                rangeLabel = getRangeLabel(st.selectedFilter, st.selectedDate)
            )
        }
    }

    private fun aggregateChartData(tasks: List<TaskModel>, filter: AnalyticsFilter, date: LocalDate): List<ChartDataPoint> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return when (filter) {
            AnalyticsFilter.WEEK -> {
                val weekStart = date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)
                (0..6).map { i ->
                    val d = weekStart.plus(i, DateTimeUnit.DAY)
                    val count = tasks.count { it.isCompleted && it.isSameDay(d) }
                    ChartDataPoint(d.dayOfWeek.name.first().toString(), count.toFloat(), d == today)
                }
            }
            AnalyticsFilter.MONTH -> {
                // Group by week of month
                val currentWeek = today.getWeekOfMonth()
                val isCurrentMonth = date.month == today.month && date.year == today.year
                
                (1..5).map { week ->
                    val firstDay = LocalDate(date.year, date.month, 1)
                    val lastDay = firstDay.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)
                    val weekStartDay = (week - 1) * 7 + 1
                    if (weekStartDay > lastDay.day) return@map null
                    val count = tasks.count { it.isCompleted && it.getWeekOfMonth() == week }
                    ChartDataPoint("W$week", count.toFloat(), isCurrentMonth && week == currentWeek)
                }.filterNotNull()
            }
            AnalyticsFilter.YEAR -> {
                val currentMonth = today.monthNumber
                val isCurrentYear = date.year == today.year
                (1..12).map { month ->
                    val count = tasks.count { it.isCompleted && it.getMonth() == month }
                    ChartDataPoint(Month(month).name.first().toString(), count.toFloat(), isCurrentYear && month == currentMonth)
                }
            }
        }
    }

    private fun LocalDate.getWeekOfMonth(): Int {
        return ((this.day - 1) / 7) + 1
    }

    private fun calculateRange(filter: AnalyticsFilter, date: LocalDate): Pair<Long, Long> {
        val timeZone = TimeZone.currentSystemDefault()
        return when (filter) {
            AnalyticsFilter.WEEK -> {
                val start = date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY).atStartOfDayIn(timeZone).toEpochMilliseconds()
                val end = date.plus(6 - date.dayOfWeek.ordinal, DateTimeUnit.DAY).atTime(23, 59, 59).toInstant(timeZone).toEpochMilliseconds()
                start to end
            }
            AnalyticsFilter.MONTH -> {
                val start = LocalDate(date.year, date.month, 1).atStartOfDayIn(timeZone).toEpochMilliseconds()
                val end = LocalDate(date.year, date.month, 1).plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY).atTime(23, 59, 59).toInstant(timeZone).toEpochMilliseconds()
                start to end
            }
            AnalyticsFilter.YEAR -> {
                val start = LocalDate(date.year, Month.JANUARY, 1).atStartOfDayIn(timeZone).toEpochMilliseconds()
                val end = LocalDate(date.year, Month.DECEMBER, 31).atTime(23, 59, 59).toInstant(timeZone).toEpochMilliseconds()
                start to end
            }
        }
    }

    private fun getRangeLabel(filter: AnalyticsFilter, date: LocalDate): String {
        return when (filter) {
            AnalyticsFilter.WEEK -> "this week"
            AnalyticsFilter.MONTH -> "this month"
            AnalyticsFilter.YEAR -> "this year"
        }
    }

    private fun calculateStreak(tasks: List<TaskModel>): Int {
        // Simplified streak logic: consecutive days with at least one completed task
        val completedDates = tasks.filter { it.isCompleted }
            .map { it.toLocalDate().toEpochDays() }
            .distinct()
            .sorted()

        if (completedDates.isEmpty()) return 0

        var maxStreak = 0
        var currentStreak = 1
        
        for (i in 1 until completedDates.size) {
            if (completedDates[i] == completedDates[i-1] + 1) {
                currentStreak++
            } else {
                maxStreak = maxOf(maxStreak, currentStreak)
                currentStreak = 1
            }
        }
        return maxOf(maxStreak, currentStreak)
    }

    private fun TaskModel.isSameDay(date: LocalDate): Boolean {
        val taskDate = Instant.fromEpochMilliseconds(this.dueDate).toLocalDateTime(TimeZone.currentSystemDefault()).date
        return taskDate == date
    }

    private fun TaskModel.getWeekOfMonth(): Int {
        val taskDate = Instant.fromEpochMilliseconds(this.dueDate).toLocalDateTime(TimeZone.currentSystemDefault()).date
        return ((taskDate.dayOfMonth - 1) / 7) + 1
    }

    private fun TaskModel.getMonth(): Int {
        return Instant.fromEpochMilliseconds(this.dueDate).toLocalDateTime(TimeZone.currentSystemDefault()).monthNumber
    }
    
    private fun TaskModel.toLocalDate(): LocalDate {
        return Instant.fromEpochMilliseconds(this.dueDate).toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
}
