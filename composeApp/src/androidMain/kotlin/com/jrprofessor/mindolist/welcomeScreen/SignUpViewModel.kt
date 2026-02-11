package com.jrprofessor.mindolist.welcomeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.usecase.CreateUserAccountUseCase
import com.jrprofessor.mindolist.domain.usecase.GetResendCooldownUseCase
import com.jrprofessor.mindolist.domain.usecase.SendOtpUseCase
import com.jrprofessor.mindolist.domain.usecase.VerifyOtpUseCase
import com.jrprofessor.mindolist.presentation.EmailButtonState
import com.jrprofessor.mindolist.presentation.SignUpEvent
import com.jrprofessor.mindolist.presentation.SignUpIntent
import com.jrprofessor.mindolist.presentation.SignUpState
import com.jrprofessor.mindolist.utils.validateEmail
import com.jrprofessor.mindolist.utils.validateOtp
import com.jrprofessor.mindolist.utils.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val verifyOtpUseCase: VerifyOtpUseCase,
    private val createUserAccountUseCase: CreateUserAccountUseCase,
    private val getResendCooldownUseCase: GetResendCooldownUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> = _state.asStateFlow()
    private val _effect = MutableSharedFlow<SignUpEvent>()
    val effect: SharedFlow<SignUpEvent> = _effect.asSharedFlow()

    private var countdownJob: Job? = null

    fun handleEvent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.EmailChanged -> onEmailChanged(intent.email)
            is SignUpIntent.OtpChanged -> onOtpChanged(intent.otp)
            is SignUpIntent.PasswordChanged -> onPasswordChanged(intent.password)

            SignUpIntent.ContinueWithEmailClicked -> continueWithEmail()
            SignUpIntent.CreateAccountClicked -> onCreateAccount()
            SignUpIntent.BackPressed -> onBackPressed()
            SignUpIntent.CreatePasswordClicked -> onCreatePassword()
            SignUpIntent.ErrorDismissed -> onErrorDismissed()
            SignUpIntent.ResendOtpClicked -> onResendOtp()
            SignUpIntent.VerifyEmailClicked -> onVerifyEmail()
        }
    }

    private fun onVerifyEmail() {
        val currentEmail = _state.value.email
        val currentOtp = _state.value.otp
        if (!validateOtp(currentOtp)) {
            _state.update { it.copy(otpError = "Please enter a valid 5-digit code") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            when (val result = verifyOtpUseCase(currentEmail, currentOtp)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            currentStep = EmailButtonState.CREATE_PASSWORD
                        )
                    }
                    stopResendCountdown()
                    sendEffect(SignUpEvent.ShowToast("Email verified successfully"))
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            otpError = result.message ?: "Invalid verification code"
                        )
                    }
                    sendEffect(
                        SignUpEvent.ShowError(
                            result.message ?: "Invalid verification code"
                        )
                    )
                }
                is Result.Loading -> {
                    // Already set loading state above
                }
            }
        }
    }

    private fun onCreateAccount() {
        val currentEmail = _state.value.email
        if (!validateEmail(currentEmail)) {
            _state.update { it.copy(emailError = "Please enter a valid email address") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = sendOtpUseCase(currentEmail)) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to send verification code"
                        )
                    }
                    sendEffect(
                        SignUpEvent.ShowError(result.message ?: "Failed to send verification code")
                    )
                }

                is Result.Loading -> {
                    // Already set loading state above
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            currentStep = EmailButtonState.VERIFY_EMAIL,
                            otpSentTimestamp = System.currentTimeMillis()
                        )
                    }
                    startResendCountdown()
                    sendEffect(SignUpEvent.ShowToast("Verification code sent to $currentEmail"))
                }
            }
        }

    }

    private fun onCreatePassword() {
        val currentEmail = _state.value.email
        val currentPassword = _state.value.password

        if (!validatePassword(currentPassword)) {
            _state.update {
                it.copy(
                    passwordError = "Password must meet all requirements"
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            when (val result = createUserAccountUseCase(currentEmail, currentPassword)) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(SignUpEvent.ShowToast("Account created successfully!"))
                    sendEffect(SignUpEvent.NavigateToHome)
                }
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message ?: "Failed to create account"
                        )
                    }
                    sendEffect(
                        SignUpEvent.ShowError(
                            result.message ?: "Failed to create account"
                        )
                    )
                }
                is Result.Loading -> {
                    // Already set loading state above
                }
            }
        }
    }

    private fun onResendOtp() {
        if (!_state.value.canResendOtp) {
            sendEffect(
                SignUpEvent.ShowToast(
                    "Please wait ${_state.value.resendCountdown} seconds before resending"
                )
            )
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = sendOtpUseCase(_state.value.email)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            otp = "",
                            otpSentTimestamp = System.currentTimeMillis()
                        )
                    }
                    startResendCountdown()
                    sendEffect(SignUpEvent.ShowToast("New verification code sent"))
                }
                is Result.Error -> {
                    _state.update { it.copy(isLoading = false) }
                    sendEffect(
                        SignUpEvent.ShowError(
                            result.message ?: "Failed to resend code"
                        )
                    )
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun onBackPressed() {
        when (_state.value.currentStep) {
            EmailButtonState.CONTINUE_WITH_EMAIL -> {
                viewModelScope.launch {
                    sendEffect(SignUpEvent.NavigateBack)
                }
            }
            EmailButtonState.CREATE_ACCOUNT -> {
                _state.update { it.copy(currentStep = EmailButtonState.CONTINUE_WITH_EMAIL) }
            }
            EmailButtonState.VERIFY_EMAIL -> {
                stopResendCountdown()
                _state.update {
                    it.copy(
                        currentStep = EmailButtonState.CREATE_ACCOUNT,
                        otp = "",
                        otpError = null
                    )
                }
            }
            EmailButtonState.CREATE_PASSWORD -> {
                _state.update {
                    it.copy(
                        currentStep = EmailButtonState.VERIFY_EMAIL,
                        password = "",
                        passwordError = null
                    )
                }
                startResendCountdown()
            }
        }
    }

    private fun onErrorDismissed() {
        _state.update {
            it.copy(
                error = null,
                emailError = null,
                otpError = null,
                passwordError = null
            )
        }
    }

    private fun startResendCountdown() {
        stopResendCountdown()

        viewModelScope.launch {
            // Check current cooldown from server
            when (val result = getResendCooldownUseCase(_state.value.email)) {
                is Result.Success -> {
                    val initialCooldown = result.data
                    if (initialCooldown > 0) {
                        _state.update {
                            it.copy(
                                resendCountdown = initialCooldown,
                                canResendOtp = false
                            )
                        }
                    }
                }
                else -> {
                    // Default to 60 seconds if we can't get server cooldown
                    _state.update {
                        it.copy(
                            resendCountdown = 60,
                            canResendOtp = false
                        )
                    }
                }
            }

            // Start countdown
            countdownJob = viewModelScope.launch {
                var countdown = _state.value.resendCountdown

                while (countdown > 0) {
                    delay(1000)
                    countdown--
                    _state.update {
                        it.copy(resendCountdown = countdown)
                    }
                }

                _state.update { it.copy(canResendOtp = true) }
            }
        }
    }

    private fun continueWithEmail() {
        _state.update {
            it.copy(currentStep = EmailButtonState.CREATE_ACCOUNT)//move to next screen on enter email field
        }
    }

    private fun onPasswordChanged(password: String) {
        if (password.length <= 12) {
            val isValid = validatePassword(password)
            _state.update {
                it.copy(
                    password = password,
                    isPasswordValid = isValid,
                    passwordError = null
                )
            }
        }
    }

    private fun onEmailChanged(email: String) {
        val isValid = validateEmail(email)
        _state.update {
            it.copy(
                email = email,
                isEmailValid = isValid,
                error = if (email.isNotEmpty() && !isValid) "Invalid email address" else null
            )
        }
    }

    private fun onOtpChanged(otp: String) {
        if (otp.length == 5 && otp.all { it.isDigit() }) {
            val valid = validateOtp(otp)
            _state.update {
                it.copy(
                    otp = otp,
                    isOtpValid = valid,
                    otpError = null,
                    error = if (!valid) "Invalid OTP" else null
                )
            }
        }
    }

    private fun sendEffect(effect: SignUpEvent) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun stopResendCountdown() {
        countdownJob?.cancel()
        countdownJob = null
    }
    override fun onCleared() {
        super.onCleared()
        stopResendCountdown()
    }
}