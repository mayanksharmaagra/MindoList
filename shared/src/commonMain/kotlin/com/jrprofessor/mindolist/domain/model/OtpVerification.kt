package com.jrprofessor.mindolist.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmField

@Serializable
data class OtpVerification(
    val email: String = "",
    val otp: String = "",
    val createdAt: Long = 0L,
    val expiresAt: Long = 0L,
    @JvmField
    val isVerified: Boolean = false
)