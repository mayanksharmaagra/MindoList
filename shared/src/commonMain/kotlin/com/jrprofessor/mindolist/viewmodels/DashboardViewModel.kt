package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.presentation.DashboardState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class DashboardViewModel(
    val firebaseAuthRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()
    init {
        loadUserData()
        updateDateTime()
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