package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.utils.Validators

class SendForgotPasswordResetLink(
    private val repository: FirebaseAuthRepository
) {
    suspend operator fun invoke(email: String): com.jrprofessor.mindolist.domain.model.Result<Unit> {
        if (email.isBlank()) {
            return com.jrprofessor.mindolist.domain.model.Result.Error(Exception("Email is required"))
        }
        if (!Validators.validateEmail(email)) {
            return com.jrprofessor.mindolist.domain.model.Result.Error(Exception("Invalid email format"))
        }
        return repository.sendPasswordResetEmail(email.trim())
    }
}