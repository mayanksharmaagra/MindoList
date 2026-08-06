package com.jrprofessor.mindolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.usecase.SendForgotPasswordResetLink
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordEvent
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordIntent
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordState
import com.jrprofessor.mindolist.presentation.forgotPassword.ForgotPasswordStep
import com.jrprofessor.mindolist.utils.Validators
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    val sendForgotPasswordResetLink: SendForgotPasswordResetLink
) : ViewModel() {
    val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()
    private val _forgotEffect = MutableSharedFlow<ForgotPasswordEvent>()
    val forgotEffect: SharedFlow<ForgotPasswordEvent> = _forgotEffect.asSharedFlow()

    fun forgotPasswordEventHandle(intent: ForgotPasswordIntent) {
        when (intent) {
            is ForgotPasswordIntent.EmailChanged -> onEmailChanged(intent.email)
            ForgotPasswordIntent.ContinueWithEmailClicked->sentResetPasswordLink()
            ForgotPasswordIntent.BackPressed -> onBackPressed()
            else -> {

            }
        }
    }

    private fun sentResetPasswordLink() {
        viewModelScope.launch {
            when (val result = sendForgotPasswordResetLink.invoke(email = _state.value.email)) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to send reset email"
                        )
                    }
                }

                is Result.Success<*> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            currentStep = ForgotPasswordStep.EMAIL_SENT
                        )
                    }
                }

                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun onBackPressed() {
        when (_state.value.currentStep) {
            ForgotPasswordStep.ENTER_EMAIL -> {
                viewModelScope.launch {
                    sendEffect(ForgotPasswordEvent.NavigateBack)
                }
            }

            else -> {
                _state.update {
                    it.copy(
                        currentStep = ForgotPasswordStep.ENTER_EMAIL,
                        email = "",
                        emailError = null,
                        isEmailValid = false
                    )
                }
            }
        }
    }

    private fun onEmailChanged(email: String) {
        val isValid = Validators.validateEmail(email)
        val error = Validators.getEmailError(email)
        _state.update {
            it.copy(
                email = email,
                isEmailValid = isValid,
                emailError = error,
            )
        }
    }

    private fun sendEffect(effect: ForgotPasswordEvent) {
        viewModelScope.launch {
            _forgotEffect.emit(effect)
        }
    }
}