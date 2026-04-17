package com.jrprofessor.mindolist.utils

object Validators {

    /**
     * Validate email address format
     */
    fun validateName(name: String): Boolean {
        return name.isNotEmpty()
    }
    /**
     * Validate email address format
     */
    fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) return false
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }

    /**
     * Validate password strength
     * Requirements: 8+ characters, 1 number, 1 symbol
     */
    fun validatePassword(password: String): Boolean {
        if (password.length < 8) return false
        val hasNumber = password.any { it.isDigit() }
        val hasSymbol = password.any { !it.isLetterOrDigit() }
        return hasNumber && hasSymbol
    }

    /**
     * Get email validation error message
     */
    fun getEmailError(email: String): String? {
        return when {
            email.isEmpty() -> null
            !validateEmail(email) -> "Invalid email address"
            else -> null
        }
    }

    /**
     * Get password validation error message
     */
    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> null
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isDigit() } -> "Password must contain a number"
            !password.any { !it.isLetterOrDigit() } -> "Password must contain a symbol"
            else -> null
        }
    }

    /**
     * Validate OTP format
     */
    fun validateOtp(otp: String, length: Int = 5): Boolean {
        return otp.length == length && otp.all { it.isDigit() }
    }
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }
}