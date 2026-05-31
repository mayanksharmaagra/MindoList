package com.jrprofessor.mindolist.presentation.login

data class LoginState(
    val currentMode: LoginMode = LoginMode.INITIAL,
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoginButtonEnabled: Boolean = false,
    val isGoogleLoading: Boolean = false,
    val isFacebookLoading: Boolean = false,
    val isAppleLoading: Boolean = false
)
enum class LoginMode  {
    INITIAL,           // Social login options screen
    EMAIL_PASSWORD     // Email/password input screen
}