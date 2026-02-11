package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    /**
     * Send OTP to mail
     * */
    suspend fun sendOtpToEmail(email: String): com.jrprofessor.mindolist.domain.model.Result<String>

    /**
     * Verify OTP
     * */
    suspend fun verifyOtp(
        email: String,
        otp: String
    ): com.jrprofessor.mindolist.domain.model.Result<Boolean>

    /**
     * Create user account with email and password
     */
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): com.jrprofessor.mindolist.domain.model.Result<User>

    /**
     * Save user to Realtime Database
     */
    suspend fun saveUserToDatabase(user: User): com.jrprofessor.mindolist.domain.model.Result<Unit>

    /**
     * Get current user
     */
    fun getCurrentUser(): Flow<User?>

    /**
     * Sign out
     */
    suspend fun signOut(): com.jrprofessor.mindolist.domain.model.Result<Unit>

    /**
     * Check if OTP is still valid
     */
    suspend fun isOtpValid(email: String): com.jrprofessor.mindolist.domain.model.Result<Boolean>

    /**
     * Get OTP resend cooldown time
     */
    suspend fun getResendCooldown(email: String): com.jrprofessor.mindolist.domain.model.Result<Int>
}