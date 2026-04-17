package com.jrprofessor.mindolist.presentation

data class SignUpState(
    val currentStep: SignUpButtonState = SignUpButtonState.CONTINUE_WITH_EMAIL,
    val name: String = "",
    val email: String = "",
    val otp: String = "",
    val password: String = "",
    val profileUrl: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val emailError: String? = null,
    val nameError: String? = null,
    val otpError: String? = null,
    val passwordError: String? = null,
    val isEmailValid: Boolean = false,
    val isNameValid: Boolean = false,
    val isOtpValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val otpSentTimestamp: Long = 0L,
    val canResendOtp: Boolean = false,
    val resendCountdown: Int = 60
)

enum class SignUpButtonState {
    CONTINUE_WITH_EMAIL,
    CREATE_ACCOUNT,
    VERIFY_EMAIL,
    CREATE_PASSWORD
}