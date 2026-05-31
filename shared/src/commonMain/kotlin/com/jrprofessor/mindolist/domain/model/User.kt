package com.jrprofessor.mindolist.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmField

@Serializable
data class User(
    val uid: String = "",
    val email: String = "",
    val profileUrl: String = "",
    val displayName: String? = null,
    val photoUrl: String? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val emailVerified: Boolean = false,
    @JvmField
    val isActive: Boolean = true,
    
    // Task Statistics
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0,
    val currentStreak: Int = 0,
    val focusRate: Double = 0.0
)