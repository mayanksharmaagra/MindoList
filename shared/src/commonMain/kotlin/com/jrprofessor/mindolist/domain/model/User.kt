package com.jrprofessor.mindolist.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmField

@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val displayName: String? = null,
    val photoUrl: String? = null,
    val createdAt: Long = 0L,  // Will be set when creating user
    val updatedAt: Long = 0L,  // Will be set when creating user
    val emailVerified: Boolean = false,
    @JvmField
    val isActive: Boolean = true
)