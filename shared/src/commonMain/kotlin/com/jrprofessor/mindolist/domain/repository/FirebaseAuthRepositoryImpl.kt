package com.jrprofessor.mindolist.domain.repository

import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.domain.model.OtpVerification
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.utils.Logger
import com.jrprofessor.mindolist.utils.toStorageData
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.database.DatabaseReference
import dev.gitlive.firebase.database.FirebaseDatabase
import dev.gitlive.firebase.storage.FirebaseStorage
import dev.gitlive.firebase.storage.StorageReference
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock.System.now
import kotlinx.coroutines.flow.combine
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.utils.NetworkConnectivityManager
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus
import kotlinx.datetime.Instant


@OptIn(ExperimentalTime::class)
private fun currentTimeMillis(): Long = now().toEpochMilliseconds()

open class FirebaseAuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseStorage: FirebaseStorage,
    private val appSettings: AppSettings,
    private val networkConnectivityManager: NetworkConnectivityManager,
) : FirebaseAuthRepository {

    private val userRef: DatabaseReference by lazy { firebaseDatabase.reference("users") }
    private val otpRef: DatabaseReference by lazy { firebaseDatabase.reference("otpVerifications") }


    companion object {
        private const val OTP_EXPIRATION_MILLIS = 600000L
        private const val RESEND_COOLDOWN = 60
    }

    override suspend fun sendOtpToEmail(email: String): Result<String> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            // Check if user already exists in Database (alternative to deprecated fetchSignInMethodsForEmail)
            val userSnapshot = userRef.orderByChild("email").equalTo(email).valueEvents.first()
            if (userSnapshot.children.any()) {
                val errorMsg = "An account with this email already exists."
                return Result.Error(Exception(errorMsg), errorMsg)
            }

            val otp = "12345"//generateOtp()

            val coolDown = getResendCooldownInternal(email)
            if (coolDown > 0) {
                val errorMsg = "Please wait $coolDown seconds before requesting a new code"
                return Result.Error(
                    Exception(errorMsg),
                    errorMsg
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

            Logger.debug { "OTP sent successfully to $email" }
            Result.Success("OTP sent to $email")
        } catch (e: Exception) {
            Logger.error(e) { "Error sending OTP" }
            Result.Error(e, "Failed to send OTP. Please try again.")
        }
    }

    override suspend fun verifyOtp(email: String, otp: String): Result<Boolean> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val snapShot = otpRef.child(sanitizeEmail(email)).valueEvents.first()


            if (!snapShot.exists) {   // ← () lagao
                val errorMsg = "No OTP found for this email"
                return Result.Error(Exception(errorMsg), errorMsg)
            }

            val otpData = snapShot.value<OtpVerification>() ?: return Result.Error(Exception("Invalid OTP data"), "Invalid OTP data")

            when {
                otpData.isVerified -> {
                    val errorMsg = "This OTP has already been used"
                    Result.Error(Exception(errorMsg), errorMsg)
                }
                otpData.otp == otp -> {
                    otpRef.child(sanitizeEmail(email))
                        .child("isVerified")
                        .setValue(true)
                    Logger.debug { "OTP verified successfully for $email" }
                    Result.Success(true)
                }
                else -> {
                    val errorMsg = "Invalid OTP. Please try again."
                    Result.Error(Exception(errorMsg), errorMsg)
                }
            }
        } catch (e: Exception) {
            Logger.error(e) { "Error verifying OTP" }
            Result.Error(e, "Failed to verify OTP")
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String,
        profileUrl: String
    ): Result<User> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val otpSnapShot = otpRef.child(sanitizeEmail(email)).valueEvents.first()
            val otpData = otpSnapShot.value<OtpVerification>()

            if (otpData == null || !otpData.isVerified) {
                val errorMsg = "Please verify your email first"
                return Result.Error(Exception(errorMsg), errorMsg)
            }

            // ✅ dev.gitlive createUserWithEmailAndPassword — no .await()
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password)
            val firebaseUser = authResult.user
                ?: return Result.Error(Exception("Failed to create user"), "Failed to create user")

            val user = User(
                uid = firebaseUser.uid,
                displayName = name,
                email = firebaseUser.email ?: "",
                emailVerified = firebaseUser.isEmailVerified,
                profileUrl=profileUrl,
                createdAt = currentTimeMillis(),
                updatedAt = currentTimeMillis()
            )

            saveUserToDatabase(user)
            otpRef.child(sanitizeEmail(email)).removeValue()

            Logger.debug { "User created successfully: ${user.uid}" }
            Result.Success(user)
        } catch (e: Exception) {
            Logger.error(e) { "Error creating user" }
            Result.Error(e, "Failed to create account: ${e.message}")
        }
    }

    override suspend fun saveUserToDatabase(user: User): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            userRef.child(user.uid).setValue(user)
            Logger.debug { "User saved successfully: ${user.uid}" }
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error saving user to database" }
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

        val userFlow = userRef.child(currentUser.uid).valueEvents.map { it.value<User>() }
        val tasksFlow = firebaseDatabase
            .reference("tasks")
            .child(currentUser.uid)
            .valueEvents.map { snapshot ->
                snapshot.children.mapNotNull { it.value<TaskModel>() }
            }

        emitAll(
            combine(userFlow, tasksFlow) { user, tasks ->
                Logger.debug { "tasks size ===== ${tasks.size}" }
                user?.copy(
                    totalTasks = tasks.size,
                    completedTasks = tasks.count { it.isCompleted },
                    pendingTasks = tasks.count { !it.isCompleted },
                    currentStreak = calculateStreak(tasks),
                    focusRate = if (tasks.isNotEmpty()) (tasks.count { it.isCompleted }.toDouble() / tasks.size * 100) else 0.0
                )
            }
        )
    }

    private fun calculateStreak(tasks: List<TaskModel>): Int {
        if (tasks.isEmpty()) return 0
        
        val completedDates = tasks.filter { it.isCompleted }
            .map { 
                Instant.fromEpochMilliseconds(it.dueDate)
                    .toLocalDateTime(TimeZone.currentSystemDefault())
                    .date 
            }
            .distinct()
            .sortedDescending()

        if (completedDates.isEmpty()) return 0

        val today = now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            
        // Streak might be broken if nothing completed today or yesterday
        if (completedDates.first() < today.minus(1, DateTimeUnit.DAY)) {
            return 0
        }

        var streak = 0
        var currentDate = completedDates.first()
        
        for (date in completedDates) {
            if (date == currentDate) {
                streak++
                currentDate = currentDate.minus(1, DateTimeUnit.DAY)
            } else {
                break
            }
        }
        return streak
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            appSettings.clear()
            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error signing out" }
            Result.Error(e, "Failed to sign out")
        }
    }

    override fun isLoggedIn(): Boolean {
        return appSettings.isLoggedIn
    }

    override fun authState(): Flow<Boolean> = flow {
        // Step 1 — Local se turant emit karo (no delay)
        emit(appSettings.isLoggedIn)

        // Step 2 — Firebase se verify karo aur sync karo
        emitAll(
            firebaseAuth.authStateChanged.map { user ->
                val loggedIn = user != null
                appSettings.isLoggedIn = loggedIn  // ← local sync
                loggedIn
            }
        )
    }

    override suspend fun uploadProfileImage(
        imageBytes: ByteArray,
        email: String?,          // ← signup time pe email pass karo
    ): Result<String> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            // uid available hai to use karo, warna email use karo
            val identifier = when {
                firebaseAuth.currentUser != null -> "uid_${firebaseAuth.currentUser?.uid}"
                email != null -> "email_${sanitizeEmail(email)}"
                else -> return Result.Error(
                    Exception("No identifier"),
                    "User not logged in and no email provided"
                )
            }

            val fileName = "profile_${identifier}_${
                currentTimeMillis()
            }.jpg"

            val ref: StorageReference = firebaseStorage.reference("profiles/$identifier/$fileName")

            ref.putData(imageBytes.toStorageData())

            val downloadUrl = ref.getDownloadUrl()
//            Logger.debug{ "download url $downloadUrl" }
            Result.Success(downloadUrl)

        } catch (e: Exception) {
            Result.Error(e, "Failed to upload image: ${e.message}")
        }
    }

    override suspend fun isOtpValid(email: String): Result<Boolean> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
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
            Logger.error(e) { "Error checking OTP validity" }
            Result.Error(e)
        }
    }

    override suspend fun getResendCooldown(email: String): Result<Int> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val cooldown = getResendCooldownInternal(email)
            Result.Success(cooldown)
        } catch (e: Exception) {
            Logger.error(e) { "Error getting resend cooldown" }
            Result.Error(e)
        }
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<User> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            // Pre-check: Verify if user exists in Database to provide specific error message
            // even if Email Enumeration Protection is enabled in Firebase Auth.
            val userSearchSnapshot = userRef.orderByChild("email").equalTo(email).valueEvents.first()
            if (!userSearchSnapshot.children.any()) {
                val errorMsg = "No account found with this email."
                return Result.Error(Exception(errorMsg), errorMsg)
            }

            // ✅ dev.gitlive — no .await()
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password)
            val firebaseUser = authResult.user ?: return Result.Error(
                Exception("Login failed"),
                "Login failed. Please try again."
            )
            appSettings.isLoggedIn = true   // ← save locally
            
            // Get user data from Database using UID
            val snapshot = userRef.child(firebaseUser.uid).valueEvents.first()
            if (!snapshot.exists) {
                // If user exists in Auth but not in DB, create a minimal record
                val newUser = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: email.substringBefore("@"),
                    photoUrl = firebaseUser.photoURL,
                    createdAt = currentTimeMillis(),
                    updatedAt = currentTimeMillis(),
                    emailVerified = firebaseUser.isEmailVerified,
                    isActive = true
                )
                saveUserToDatabase(newUser)
                return Result.Success(newUser)
            }

            val user = snapshot.value<User>() ?: return Result.Error(
                Exception("User data corrupted"),
                "User profile not found. Please contact support."
            )
            
            Result.Success(user)
        } catch (e: Exception) {
            Logger.error(e) { "Login error: ${e.message}" }
            Result.Error(e, parseLoginError(e.message))
        }
    }

    private fun parseLoginError(message: String?): String {
        val msg = message?.lowercase() ?: ""
        return when {
            msg.contains("user-not-found") || msg.contains("no user record") -> "No account found with this email."
            msg.contains("wrong-password") || msg.contains("invalid-credential") || msg.contains("auth credential") -> "Invalid email or password."
            msg.contains("user-disabled") -> "This account has been disabled."
            msg.contains("too-many-requests") -> "Too many failed attempts. Please try again later."
            msg.contains("network") -> "Network error. Please check your connection."
            else -> "Login failed. ${message ?: "Please try again."}"
        }
    }

    override suspend fun resetPassword(
        email: String,
        password: String
    ): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            // STEP 1: Get current user
            val currentUser = firebaseAuth.currentUser
                ?: return Result.Error(
                    Exception("No authenticated user"),
                    "User session expired. Please try again."
                )

            // STEP 2: Update password (dev.gitlive methods are already suspend)
            currentUser.updatePassword(password)
            Logger.debug { "Password updated in Firebase Auth for: $email" }

            // STEP 3: Update metadata in Realtime DB
            val updates = mapOf<String, Any?>(
                "updatedAt" to currentTimeMillis(),
                "resetOTP" to null  // Clear OTP after successful reset
            )

            userRef.child(currentUser.uid).updateChildren(updates)
            Logger.debug { "Password reset metadata updated in DB for UID: ${currentUser.uid}" }

            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error resetting password: ${e.message}" }
            Result.Error(e, "Failed to reset password. ${e.message}")
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
        Logger.debug { "OTP for $toEmail: $otp" }
    }
    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            firebaseAuth.sendPasswordResetEmail(email)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, parsePasswordResetError(e.message))
        }
    }

    override suspend fun deleteAccount(): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val currentUser = firebaseAuth.currentUser
                ?: return Result.Error(Exception("No authenticated user"), "No user found")

            val uid = currentUser.uid
            // Delete from Database
            userRef.child(uid).removeValue()
            firebaseDatabase.reference("tasks").child(uid).removeValue()

            // Delete from Auth
            currentUser.delete()
            appSettings.clear()

            Result.Success(Unit)
        } catch (e: Exception) {
            Logger.error(e) { "Error deleting account" }
            Result.Error(e, "Failed to delete account. ${e.message}")
        }
    }

    override suspend fun updateGoogleIntegration(googleEmail: String, accessToken: String): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: return Result.Error(Exception("Not logged in"))
            val updates = mapOf<String, Any?>(
                "googleEmail" to googleEmail,
                "googleAccessToken" to accessToken,
                "isGoogleConnected" to true,
                "googleLinkedAt" to currentTimeMillis()
            )
            userRef.child(uid).updateChildren(updates)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to update Google integration")
        }
    }

    override suspend fun disconnectGoogleIntegration(): Result<Unit> {
        if (!networkConnectivityManager.isNetworkAvailable()) {
            return Result.Error(Exception("No internet connection"), "No internet connection")
        }
        return try {
            val uid = firebaseAuth.currentUser?.uid ?: return Result.Error(Exception("Not logged in"))
            val updates = mapOf<String, Any?>(
                "googleEmail" to null,
                "googleAccessToken" to null,
                "isGoogleConnected" to false,
                "googleLinkedAt" to 0L
            )
            userRef.child(uid).updateChildren(updates)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e, "Failed to disconnect Google integration")
        }
    }

    private fun parsePasswordResetError(message: String?): String {
        return when {
            message?.contains("no user record") == true -> "No account found with this email"
            message?.contains("network error") == true -> "No internet connection"
            message?.contains("invalid email") == true -> "Invalid email format"
            else -> "Failed to send reset email. Please try again"
        }
    }
}