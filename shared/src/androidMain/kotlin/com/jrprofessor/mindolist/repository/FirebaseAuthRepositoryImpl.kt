package com.jrprofessor.mindolist.repository

import android.system.Os.close
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.jrprofessor.mindolist.domain.model.OtpVerification
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Properties
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Singleton
class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
) : FirebaseAuthRepository {
    private val userRef: DatabaseReference = Firebase.database.getReference("users")
    private val otpRef: DatabaseReference = firebaseDatabase.getReference("otpVerifications")

    companion object {
        private const val TAG = "FirebaseAuthRepository"
        private const val OTP_EXPIRATION_SECONDS = 600000L
        private const val OTP_VERIFICATION_CODE_LENGTH = 5
        private const val RESEND_COOLDOWN = 60 // 60 seconds
    }


    override suspend fun sendOtpToEmail(email: String): Result<String> {
        return try {
            // generate 5 digit otp
            val otp = generateOtp()

            // Check if there's an existing OTP with cooldown
            val coolDown = getResendCooldownInternal(email)
            if (coolDown > 0) {
                return Result.Error(
                    Exception("Please wait $coolDown seconds before requesting a new code")
                )
            }

            //Create otp verification object
            val otpVerification = OtpVerification(
                email = email,
                otp = otp,
                createdAt = System.currentTimeMillis(),
                expiresAt = System.currentTimeMillis() + OTP_EXPIRATION_SECONDS,
                isVerified = false
            )
            // Save the OTP to the database
            otpRef.child(sanitizeEmail(email)).setValue(otpVerification).await()
            // Send email (you'll need to implement email sending)
            sendOtpEmail(email, otp)

            Log.d(TAG, "OTP sent successfully to $email")
            Result.Success("OTP sent to $email")
        } catch (e: Exception) {
            Log.e(TAG, "Error sending OTP", e)
            Result.Error(e, "Failed to send OTP. Please try again.")
        }
    }

    override suspend fun verifyOtp(
        email: String,
        otp: String
    ): Result<Boolean> {
        return try {
            val snapShot = otpRef.child(sanitizeEmail(email)).get().await()
            if (!snapShot.exists()) {
                return Result.Error(Exception("No otp found for this email"))
            }
            val otpData = snapShot.getValue(OtpVerification::class.java)
                ?: return Result.Error(Exception("Invalid OTP data"))
            if (otpData.otp == otp && !otpData.isVerified) {
                // Mark otp is verified
                otpRef.child(sanitizeEmail(email))
                    .child("isVerified")
                    .setValue(true)
                    .await()
                Log.d(TAG, "OTP verified successfully for $email")
                Result.Success(true)
            } else if (otpData.isVerified) {
                Result.Error(Exception("This OTP has already been used"))
            } else {
                Result.Error(Exception("Invalid OTP. Please try again."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error verifying OTP", e)
            Result.Error(e, "Failed to verify OTP")
        }
    }

    override suspend fun createUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): Result<User> {
        return try {

            val otpSnapShot = otpRef.child(sanitizeEmail(email)).get().await()
            val otpData = otpSnapShot.getValue(OtpVerification::class.java)

            if (otpData == null || !otpData.isVerified) {
                return Result.Error(Exception("Please verify your email first"))
            }
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser =
                authResult.user ?: return Result.Error(Exception("Failed to create user"))
            val user = User(
                uid = firebaseUser.uid,
                displayName = name,
                email = firebaseUser.email ?: "",
                emailVerified = firebaseUser.isEmailVerified,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            //save User into database
            saveUserToDatabase(user)
            // Clean up OTP data
            otpRef.child(sanitizeEmail(email)).removeValue().await()

            Log.d(TAG, "User created successfully: ${user.uid}")
            Result.Success(user)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating user", e)
            Result.Error(e, "Failed to create account: ${e.localizedMessage}")
        }
    }

    override suspend fun saveUserToDatabase(user: User): Result<Unit> {
        return try {
            userRef.child(user.uid).setValue(user).await()
            Log.d(TAG, "User saved successfully: ${user.uid}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving user to database", e)
            Result.Error(e, "Failed to save user data")
        }
    }

    override fun getCurrentUser(): Flow<User?> = callbackFlow {
        val currentUser = firebaseAuth.currentUser
        if(currentUser==null){
            trySend(null)
            close()
            return@callbackFlow
        }
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                trySend(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Error getting current user", error.toException())
                close(error.toException())
            }
        }
        userRef.child(currentUser.uid).addValueEventListener(listener)

        awaitClose {
            userRef.child(currentUser.uid).removeEventListener(listener)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Error signing out", e)
            Result.Error(e, "Failed to sign out")
        }
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun isOtpValid(email: String): Result<Boolean> {
        return try {
            val snapshot = otpRef.child(sanitizeEmail(email)).get().await()

            if (!snapshot.exists()) {
                return Result.Success(false)
            }

            val otpData = snapshot.getValue(OtpVerification::class.java)
                ?: return Result.Success(false)

            val isValid = System.currentTimeMillis() <= otpData.expiresAt && !otpData.isVerified
            Result.Success(isValid)

        } catch (e: Exception) {
            Log.e(TAG, "Error checking OTP validity", e)
            Result.Error(e)
        }
    }

    override suspend fun getResendCooldown(email: String): Result<Int> {
        return try {
            val cooldown = getResendCooldownInternal(email)
            Result.Success(cooldown)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting resend cooldown", e)
            Result.Error(e)
        }
    }

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<User> {

        return try {

            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val uid = authResult.user?.uid
                    ?: return Result.Error(Exception("User ID not found"))
                val user = User(
                    uid = uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName,
                    photoUrl = firebaseUser.photoUrl?.toString(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    emailVerified = firebaseUser.isEmailVerified,
                    isActive = true
                )
                Result.Success(user)
            } else {
                Log.e(TAG, "Error getting: "+ "Login failed")
                Result.Error(Exception("Login failed"))
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error getting: "+ e.message)
            Result.Error(e)
        }
    }


    private suspend fun getResendCooldownInternal(email: String): Int {
        val snapshot = otpRef.child(sanitizeEmail(email)).get().await()

        if (!snapshot.exists()) {
            return 0
        }

        val otpData = snapshot.getValue(OtpVerification::class.java) ?: return 0

        val elapsedSeconds = (System.currentTimeMillis() - otpData.createdAt) / 1000
        val remainingCooldown = RESEND_COOLDOWN - elapsedSeconds.toInt()

        return if (remainingCooldown > 0) remainingCooldown else 0
    }

    private fun sanitizeEmail(email: String): String {
        return email.replace(".", "_").replace("@", "_at_")
    }

    private fun generateOtp(): String {
        return (10000..99999).random().toString()
    }

    /**
     * Send OTP via email
     * NOTE: For production, use a proper email service like SendGrid, AWS SES, or Firebase Cloud Functions
     * This is a simple implementation using JavaMail API
     */
    private suspend fun sendOtpEmail(toEmail: String, otp: String) {
        try {
            // TODO: Replace with your email configuration
            // For production, move this to Firebase Cloud Functions
            val senderEmail = "mayanksharmaagra@gmail.com"
            // This is an app password, not your regular password.
            // Go to your Google account settings -> Security -> 2-Step Verification -> App passwords
            val senderPassword = "dmvk uzjp wbfx vqeg" // TODO: Replace with your app password

            val props = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.port", "587")
            }

            val session = Session.getInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(senderEmail, senderPassword)
                }
            })

            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(senderEmail))
                setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                subject = "Your Verification Code"
                setText("""
                    Your verification code is: $otp
                    
                    This code will expire in 10 minutes.
                    
                    If you didn't request this code, please ignore this email.
                """.trimIndent())
            }

            // Send email in background thread
            Dispatchers.IO.let {
                withContext(it) {
                    Transport.send(message)
                }
            }

            Log.d(TAG, "OTP email sent to $toEmail")

        } catch (e: Exception) {
            Log.e(TAG, "Error sending OTP email", e)
            // For development, just log the OTP
            Log.d(TAG, "OTP for $toEmail: $otp")
        }
    }

}