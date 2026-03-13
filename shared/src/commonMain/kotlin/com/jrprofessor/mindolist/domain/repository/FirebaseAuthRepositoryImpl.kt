package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.domain.model.OtpVerification
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.model.User
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import dev.gitlive.firebase.database.database
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


private val logger = KotlinLogging.logger {

}



@OptIn(ExperimentalTime::class)
private fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()

open class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
) : FirebaseAuthRepository {

    private val userRef: DatabaseReference = firebaseDatabase.reference("users")
    private val otpRef: DatabaseReference = firebaseDatabase.reference("otpVerifications")


    companion object {
        private const val OTP_EXPIRATION_MILLIS = 600000L
        private const val RESEND_COOLDOWN = 60
    }

    override suspend fun sendOtpToEmail(email: String): Result<String> {
        return try {
            val otp = generateOtp()

            val coolDown = getResendCooldownInternal(email)
            if (coolDown > 0) {
                return Result.Error(
                    Exception("Please wait $coolDown seconds before requesting a new code")
                )
            }

            val otpVerification = OtpVerification(
                email = email,
                otp = otp,
                createdAt = currentTimeMillis(),        // ✅ expect/actual
                expiresAt = currentTimeMillis() + OTP_EXPIRATION_MILLIS,
                isVerified = false
            )

            // ✅ dev.gitlive me directly setValue (no .await() needed)
            otpRef.child(sanitizeEmail(email)).setValue(otpVerification)
            sendOtpEmail(email, otp)

            logger.debug { "OTP sent successfully to $email" }
            Result.Success("OTP sent to $email")
        } catch (e: Exception) {
            logger.error(e) { "Error sending OTP" }
            Result.Error(e, "Failed to send OTP. Please try again.")
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<Boolean> {
        return try {
            val snapShot = otpRef.child(sanitizeEmail(email)).valueEvents.first()


            if (!snapShot.exists) {   // ← () lagao
                return Result.Error(Exception("No OTP found for this email"))
            }

            val otpData = snapShot.value<OtpVerification>()
                ?: return Result.Error(Exception("Invalid OTP data"))

            when {
                otpData.isVerified -> {
                    Result.Error(Exception("This OTP has already been used"))
                }
                otpData.otp == otp -> {
                    otpRef.child(sanitizeEmail(email))
                        .child("isVerified")
                        .setValue(true)
                    logger.debug { "OTP verified successfully for $email" }
                    Result.Success(true)
                }
                else -> {
                    Result.Error(Exception("Invalid OTP. Please try again."))
                }
            }
        } catch (e: Exception) {
            logger.error(e) { "Error verifying OTP" }
            Result.Error(e, "Failed to verify OTP")
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): Result<User> {
        return try {
            val otpSnapShot = otpRef.child(sanitizeEmail(email)).valueEvents.first()
            val otpData = otpSnapShot.value<OtpVerification>()

            if (otpData == null || !otpData.isVerified) {
                return Result.Error(Exception("Please verify your email first"))
            }

            // ✅ dev.gitlive createUserWithEmailAndPassword — no .await()
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password)
            val firebaseUser = authResult.user
                ?: return Result.Error(Exception("Failed to create user"))

            val user = User(
                uid = firebaseUser.uid,
                displayName = name,
                email = firebaseUser.email ?: "",
                emailVerified = firebaseUser.isEmailVerified,
                createdAt = currentTimeMillis(),
                updatedAt = currentTimeMillis()
            )

            saveUserToDatabase(user)
            otpRef.child(sanitizeEmail(email)).removeValue()

            logger.debug { "User created successfully: ${user.uid}" }
            Result.Success(user)
        } catch (e: Exception) {
            logger.error(e) { "Error creating user" }
            Result.Error(e, "Failed to create account: ${e.message}")
        }
    }

    override suspend fun saveUserToDatabase(user: User): Result<Unit> {
        return try {
            userRef.child(user.uid).setValue(user)
            logger.debug { "User saved successfully: ${user.uid}" }
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(e) { "Error saving user to database" }
            Result.Error(e, "Failed to save user data")
        }
    }

    // ✅ dev.gitlive Flow support — ValueEventListener nahi chahiye
    override fun getCurrentUser(): Flow<User?> = flow {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            emit(null)
            return@flow
        }
        // ✅ dev.gitlive database flow
        userRef.child(currentUser.uid)
            .valueEvents
            .collect { snapshot ->
                val user = snapshot.value<User>()
                emit(user)
            }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            logger.error(e) { "Error signing out" }
            Result.Error(e, "Failed to sign out")
        }
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun authState(): Flow<Boolean> = flow {
        val current = firebaseAuth.currentUser
        println(">>> currentUser: $current")  // ← check
        emit(current != null)
        emitAll(firebaseAuth.authStateChanged.map {
            println(">>> authStateChanged: $it")  // ← check
            it != null
        })
    }

    override suspend fun isOtpValid(email: String): Result<Boolean> {
        return try {
            val snapshot = otpRef.child(sanitizeEmail(email)).valueEvents.first()

            if (!snapshot.exists) {
                return Result.Success(false)
            }

            val otpData = snapshot.value<OtpVerification>()
                ?: return Result.Success(false)

            val isValid = currentTimeMillis() <= otpData.expiresAt && !otpData.isVerified
            Result.Success(isValid)
        } catch (e: Exception) {
            logger.error(e) { "Error checking OTP validity" }
            Result.Error(e)
        }
    }

    override suspend fun getResendCooldown(email: String): Result<Int> {
        return try {
            val cooldown = getResendCooldownInternal(email)
            Result.Success(cooldown)
        } catch (e: Exception) {
            logger.error(e) { "Error getting resend cooldown" }
            Result.Error(e)
        }
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<User> {
        return try {
            // ✅ dev.gitlive — no .await()
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password)
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName,
                    photoUrl = firebaseUser.photoURL,
                    createdAt = currentTimeMillis(),
                    updatedAt = currentTimeMillis(),
                    emailVerified = firebaseUser.isEmailVerified,
                    isActive = true
                )
                Result.Success(user)
            } else {
                Result.Error(Exception("Login failed"), "Login failed")
            }
        } catch (e: Exception) {
            logger.error(e) { "Login error: ${e.message}" }
            Result.Error(e, e.message ?: "Login failed. Please try again.")

        }
    }

    // ─── Private Helpers ──────────────────────────────

    private suspend fun getResendCooldownInternal(email: String): Int {
        val snapshot = otpRef.child(sanitizeEmail(email)).valueEvents.first()

        if (!snapshot.exists) return 0

        val otpData = snapshot.value<OtpVerification>() ?: return 0

        val elapsedSeconds = (currentTimeMillis() - otpData.createdAt) / 1000
        val remainingCooldown = RESEND_COOLDOWN - elapsedSeconds.toInt()

        return if (remainingCooldown > 0) remainingCooldown else 0
    }

    private fun sanitizeEmail(email: String): String {
        return email.replace(".", "_").replace("@", "_at_")
    }

    private fun generateOtp(): String {
        return (10000..99999).random().toString()
    }

    // ✅ Email sending — androidMain me JavaMail implement karo
    suspend fun sendOtpEmail(toEmail: String, otp: String) {
        // Override this in DI or use a platform-specific email service
        logger.debug { "OTP for $toEmail: $otp" }
    }
}