package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.utils.GoogleSignInBridge

// commonMain/kotlin/.../di/PlatformBridgeHolder.kt
object PlatformBridgeHolder {
    lateinit var googleSignInBridge: GoogleSignInBridge
}