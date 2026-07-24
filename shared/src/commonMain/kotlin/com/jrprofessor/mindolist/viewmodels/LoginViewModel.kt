package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.usecase.LoginWithEmailUseCase
import com.jrprofessor.mindolist.presentation.login.LoginEvent
import com.jrprofessor.mindolist.presentation.login.LoginIntent
import com.jrprofessor.mindolist.presentation.login.LoginMode
import com.jrprofessor.mindolist.presentation.login.LoginState
import com.jrprofessor.mindolist.utils.Validators
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel (
    private val loginWithEmailUseCase: LoginWithEmailUseCase,
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _loginEffect = MutableSharedFlow<LoginEvent>()
    val loginEffect: SharedFlow<LoginEvent> = _loginEffect.asSharedFlow()
    fun handleLoginEvent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.EmailChanged -> onEmailChanged(intent.email)
            is LoginIntent.PasswordChanged -> onPasswordChanged(intent.password)
            is LoginIntent.TogglePasswordVisibility -> onTogglePasswordVisibility()
            is LoginIntent.LoginClicked -> onLogin()
            is LoginIntent.ContinueWithEmailClicked -> onContinueWithEmail()
            is LoginIntent.ForgotPasswordClicked -> onForgotPassword()
            is LoginIntent.BackPressed -> onBackPressed()
            is LoginIntent.ErrorDismissed -> onErrorDismissed()
            is LoginIntent.ClearState -> onClearState()
        }
    }

    private fun onClearState() {
        _loginState.update { LoginState() }
    }

    private fun onLogin() {
        viewModelScope.launch {
            _loginState.update {
                it.copy(isLoading = true, error = null)
            }
            val result = loginWithEmailUseCase(
                email = _loginState.value.email,
                password = _loginState.value.password
            )
            when (result) {
                is Result.Error -> {
                    _loginState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Login failed. Please try again."
                        )
                    }
                    sendEffect(
                        LoginEvent.ShowError(
                            result.message ?: "Login failed. Please try again."
                        )
                    )
                }

                is Result.Success<*> -> {
                    _loginState.update { it.copy(isLoading = false) }
                    _loginEffect.emit(LoginEvent.NavigateToHome)
                }

                else -> {
                    _loginState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun onContinueWithEmail() {
        _loginState.update {
            it.copy(currentMode = LoginMode.EMAIL_PASSWORD)
        }
    }

    private fun onForgotPassword() {
        viewModelScope.launch {
            _loginEffect.emit(LoginEvent.NavigateToForgotPassword)
        }
    }

    private fun onBackPressed() {
        viewModelScope.launch {
            if (_loginState.value.currentMode == LoginMode.EMAIL_PASSWORD) {
                // Go back to initial screen
                _loginState.update {
                    it.copy(
                        currentMode = LoginMode.INITIAL,
                        email = "",
                        password = "",
                        emailError = null,
                        passwordError = null,
                        error = null
                    )
                }
            } else {
                // Exit login screen
                _loginEffect.emit(LoginEvent.NavigateBack)
            }
        }
    }

    private fun onErrorDismissed() {
        _loginState.update {
            it.copy(
                error = null,
                emailError = null,
                passwordError = null
            )
        }
    }

    private fun onPasswordChanged(password: String) {
        val isValid = Validators.validatePassword(password)
        _loginState.update {
            it.copy(
                password = password,
                isPasswordValid = isValid,
                passwordError = null,
                isLoginButtonEnabled = it.isEmailValid && isValid
            )
        }
    }

    private fun onEmailChanged(email: String) {
        val isValid = Validators.validateEmail(email)
        _loginState.update {
            it.copy(
                email = email,
                isEmailValid = isValid,
                emailError = if (email.isNotEmpty() && !isValid) "Invalid email address" else null,
                isLoginButtonEnabled = isValid && it.isPasswordValid
            )
        }
    }

    // ✅ Login specific - uses LoginState
    private fun onTogglePasswordVisibility() {
        _loginState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }


    private fun sendEffect(effect: LoginEvent) {
        viewModelScope.launch {
            _loginEffect.emit(effect)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}