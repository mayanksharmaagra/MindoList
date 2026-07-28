package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.GoogleCalendarRepository
import com.jrprofessor.mindolist.model.GoogleItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoogleCalendarViewModel(
    private val repository: GoogleCalendarRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<GoogleItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun onGoogleSignInSuccess(accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _items.value = repository.fetchAll(accessToken)
            } catch (e: Exception) {
                _error.value = "Failed to fetch Google items: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearItems() {
        _items.value = emptyList()
    }

    fun clearError() {
        _error.value = null
    }
}
