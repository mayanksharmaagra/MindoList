package com.jrprofessor.mindolist.domain.usecase

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository


class CreateUserAccountUseCase(private val firebaseAuthRepository: FirebaseAuthRepository) {

    suspend operator fun invoke(name: String, email: String, password: String, profileUrl: String): Result<User> {
        if (password.length < 8) {
            return Result.Error(Exception("Password must be at least 8 characters"))
        }

        if (!password.any { it.isDigit() }) {
            return Result.Error(Exception("Password must contain at least one number"))
        }

        if (!password.any { !it.isLetterOrDigit() }) {
            return Result.Error(Exception("Password must contain at least one symbol"))
        }

        return firebaseAuthRepository.createUserWithEmailAndPassword(name,email,password,profileUrl)
    }
}