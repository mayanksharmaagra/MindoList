package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository


class VerifyOtpUseCase(private val firebaseAuthRepository: FirebaseAuthRepository) {

    suspend operator fun invoke(email: String,otp: String): Result<Boolean> {
        if (otp.length!=5 || !otp.all { it.isDigit() }) {
            return Result.Error(Exception("Please enter a valid 5-digit code"))
        }

        return firebaseAuthRepository.verifyOtp(email,otp)
    }
}