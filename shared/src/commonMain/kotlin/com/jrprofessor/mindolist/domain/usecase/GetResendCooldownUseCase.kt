package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository


class GetResendCooldownUseCase(private val firebaseAuthRepository: FirebaseAuthRepository) {

    suspend operator fun invoke(email: String): Result<Int> {
        return firebaseAuthRepository.getResendCooldown(email)
    }
}