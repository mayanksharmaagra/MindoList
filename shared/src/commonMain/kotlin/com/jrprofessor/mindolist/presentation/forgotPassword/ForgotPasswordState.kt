package com.jrprofessor.mindolist.presentation.forgotPassword

data class ForgotPasswordState(
    val email: String = "",
    val otp: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmPasswordValid: Boolean = false,
    val emailError: String? = null,
    val otpError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isOtpValid: Boolean = false,
    val currentStep: ForgotPasswordStep = ForgotPasswordStep.ENTER_EMAIL,
    val error: String? = null,
    val otpSentTimestamp: Long = 0L,
    val canResendOtp: Boolean = false,
    val resendCountdown: Int = 60
)
enum class ForgotPasswordStep {
    ENTER_EMAIL,    // Screen 1: Enter email
    EMAIL_SENT,
    VERIFY_EMAIL,
    UPDATE_PASSWORD,
    SHOW_SUCCESS
}