package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.model.Result

/**
 * Login with email and password
 */
class LoginWithEmailUseCase(
    private val repository: FirebaseAuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // ✅ Final validation
        if (email.isEmpty()) {
            return Result.Error(Exception("Email is required"))
        }

        if (!isValidEmail(email)) {
            return Result.Error(Exception("Invalid email format"))
        }

        if (password.isEmpty()) {
            return Result.Error(Exception("Password is required"))
        }

        // ✅ Call repository
        return repository.loginWithEmailAndPassword(email, password)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }
}