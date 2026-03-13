package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.model.User
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    /**
     * Send OTP to mail
     * */
    suspend fun sendOtpToEmail(email: String): Result<String>

    /**
     * Verify OTP
     * */
    suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<Boolean>

    /**
     * Create user account with email and password
     */
    suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): Result<User>

    /**
     * Save user to Realtime Database
     */
    suspend fun saveUserToDatabase(user: User): Result<Unit>

    /**
     * Check if OTP is still valid
     */
    suspend fun isOtpValid(email: String): Result<Boolean>

    /**
     * Get OTP resend cooldown time
     */
    suspend fun getResendCooldown(email: String): Result<Int>

    /**
     * Login with email and password
     */
    suspend fun loginWithEmailAndPassword(email: String, password: String): Result<User>

    // ─── Session ──────────────────────────────────────
    /**
     * Get current user
     */
    fun getCurrentUser(): Flow<User?>

    /**
     * Sign out
     */
    suspend fun signOut(): Result<Unit>
    fun isLoggedIn(): Boolean
    fun authState(): Flow<Boolean>

}