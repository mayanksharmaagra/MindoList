package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.model.TaskModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock

data class AnalyticsState(
    val selectedPeriod: String = "Monthly",
    val completionPercentage: Int = 0,
    val totalTasks: Int = 0,
    val currentStreak: Int = 0,
    val categoryProgress: List<CategoryStats> = emptyList(),
    val isLoading: Boolean = false,
    val trendMessage: String = ""
)

data class CategoryStats(
    val name: String,
    val progress: Float,
    val color: String // Optional, can be handled in UI
)

class AnalyticsViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AnalyticsState())
    val state: StateFlow<AnalyticsState> = _state.asStateFlow()

    private var allTasks: List<TaskModel> = emptyList()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            taskRepository.getTasks().collect { result ->
                if (result is Result.Success) {
                    allTasks = result.data
                    processAnalytics()
                }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun setPeriod(period: String) {
        _state.update { it.copy(selectedPeriod = period) }
        processAnalytics()
    }

    private fun processAnalytics() {
        val filteredTasks = filterTasksByPeriod(allTasks, _state.value.selectedPeriod)
        
        val total = filteredTasks.size
        val completed = filteredTasks.count { it.isCompleted }
        val percentage = if (total > 0) (completed * 100) / total else 0

        val categoryStats = filteredTasks.groupBy { it.category }
            .map { (category, tasks) ->
                val catTotal = tasks.size
                val catCompleted = tasks.count { it.isCompleted }
                CategoryStats(
                    name = category,
                    progress = if (catTotal > 0) catCompleted.toFloat() / catTotal else 0f,
                    color = "" 
                )
            }.sortedByDescending { it.progress }

        val streak = calculateStreak(allTasks)

        _state.update {
            it.copy(
                totalTasks = total,
                completionPercentage = percentage,
                categoryProgress = categoryStats,
                currentStreak = streak,
                trendMessage = calculateTrend(allTasks, _state.value.selectedPeriod)
            )
        }
    }

    private fun filterTasksByPeriod(tasks: List<TaskModel>, period: String): List<TaskModel> {
        val startOfToday = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        
        return when (period) {
            "Daily" -> tasks.filter { it.dueDate >= startOfToday && it.dueDate < startOfToday + 86400000 }
            "Weekly" -> tasks.filter { it.dueDate >= startOfToday - (7 * 86400000) }
            "Monthly" -> tasks.filter { it.dueDate >= startOfToday - (30L * 86400000L) }
            else -> tasks
        }
    }

    private fun calculateStreak(tasks: List<TaskModel>): Int {
        val completedDates = tasks.filter { it.isCompleted }
            .map { kotlinx.datetime.Instant.fromEpochMilliseconds(it.dueDate).toLocalDateTime(TimeZone.currentSystemDefault()).date }
            .distinct()
            .sortedDescending()

        if (completedDates.isEmpty()) return 0

        var streak = 0
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        var currentDate: LocalDate

        // Check if the streak is still active (completed today or yesterday)
        if (completedDates.first() != today && completedDates.first() != today.minus(1, DateTimeUnit.DAY)) {
            // Streak broken, but we still count the consecutive days from the last completion if active
            // For now, let's just count backwards from the most recent completion if it was recent enough
            if (completedDates.first() < today.minus(1, DateTimeUnit.DAY)) return 0
            currentDate = completedDates.first()
        } else {
            currentDate = completedDates.first()
        }

        for (date in completedDates) {
            if (date == currentDate) {
                streak++
                currentDate = currentDate.minus(1, DateTimeUnit.DAY)
            } else {
                break
            }
        }
        return streak
    }

    private fun calculateTrend(tasks: List<TaskModel>, period: String): String {
        // Mock trend logic for now
        return "↗ 12% more than last $period"
    }
}
