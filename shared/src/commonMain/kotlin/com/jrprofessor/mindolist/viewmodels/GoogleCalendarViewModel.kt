package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.UnauthorizedException
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.repository.GoogleCalendarRepository
import com.jrprofessor.mindolist.model.GoogleItem
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoogleCalendarViewModel(
    private val repository: GoogleCalendarRepository,
    private val firebaseAuthRepository: FirebaseAuthRepository,
    private val authManager: GoogleAuthManager
) : ViewModel() {

    private val _items = MutableStateFlow<List<GoogleItem>>(emptyList())
    val items = _items.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun onGoogleSignInSuccess(accessToken: String, isRetry: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _items.value = repository.fetchAll(accessToken)
            } catch (_: UnauthorizedException) {
                if (!isRetry) {
                    val newToken = authManager.refreshAccessToken(accessToken)
                    if (newToken != null) {
                        // Update persisted token in Firebase
                        val googleEmail = authManager.userData.value?.googleEmail
                        if (googleEmail != null) {
                            firebaseAuthRepository.updateGoogleIntegration(googleEmail, newToken)
                        }
                        onGoogleSignInSuccess(newToken, isRetry = true)
                    } else {
                        _error.value = "Google session expired. Please reconnect your account."
                    }
                } else {
                    _error.value = "Google session expired. Please reconnect your account."
                }
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

    fun updateGoogleIntegration(googleEmail: String, accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = firebaseAuthRepository.updateGoogleIntegration(googleEmail, accessToken)
                if (result is com.jrprofessor.mindolist.domain.model.Result.Success) {
                    onGoogleSignInSuccess(accessToken)
                } else if (result is com.jrprofessor.mindolist.domain.model.Result.Error) {
                    _error.value = result.message ?: "Failed to update Google integration"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update Google integration"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun disconnectGoogleIntegration() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = firebaseAuthRepository.disconnectGoogleIntegration()
                if (result is com.jrprofessor.mindolist.domain.model.Result.Success) {
                    authManager.signOut()
                    clearItems()
                } else if (result is com.jrprofessor.mindolist.domain.model.Result.Error) {
                    _error.value = result.message ?: "Failed to disconnect Google integration"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to disconnect Google integration"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
