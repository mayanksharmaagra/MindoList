package com.jrprofessor.mindolist.utils

object Validators {

    /**
     * Validate email address format
     */
    fun validateName(name: String): Boolean {
        return name.isNotEmpty() && name.length <= 50
    }
    /**
     * Validate email address format
     */
    fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) return false
        if (email.length > 50) return false
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
            email.length > 50 -> "Email must be 50 characters or less"
            !validateEmail(email) -> "Invalid email address"
            else -> null
        }
    }

    /**
     * Get name validation error message
     */
    fun getNameError(name: String): String? {
        return when {
            name.isEmpty() -> null
            name.length > 50 -> "Name must be 50 characters or less"
            else -> null
        }
    }

    /**
     * Get password validation error message
     */
    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> "Password is required"
            password.length < 8 -> "Password must be at least 8 characters"
            !password.any { it.isDigit() } -> "Include at least one number"
            !password.any { !it.isLetterOrDigit() } -> "Include at least one symbol (@, #, $, etc.)"
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

    /**
     * Validate Task Title
     */
    fun validateTaskTitle(title: String): Boolean {
        return title.isNotBlank() && title.length <= 100
    }

    /**
     * Validate Task Description
     */
    fun validateTaskDescription(description: String): Boolean {
        return description.length <= 500
    }

    /**
     * Check if the date is in the past
     */
    fun isDateInPast(dueDateMillis: Long): Boolean {
        if (dueDateMillis == 0L) return false
        val now = kotlin.time.Clock.System.now().toEpochMilliseconds()
        // Allow a small grace period (e.g. 1 minute)
        return dueDateMillis < (now - 60000L)
    }
}