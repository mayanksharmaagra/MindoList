package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository


class SendOtpUseCase(private val firebaseAuthRepository: FirebaseAuthRepository) {

    suspend operator fun invoke(email: String): Result<String> {
        if (!isValidEmail(email)) {
            return Result.Error(Exception("Enter valid Email Address"))
        }

        return firebaseAuthRepository.sendOtpToEmail(email)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }
}