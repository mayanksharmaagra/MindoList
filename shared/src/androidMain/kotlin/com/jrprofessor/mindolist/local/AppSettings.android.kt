package com.jrprofessor.mindolist.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

actual class SettingsDelegate(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("mindolist_prefs", Context.MODE_PRIVATE)

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        prefs.getBoolean(key, defaultValue)

    actual fun putBoolean(key: String, value: Boolean) =
        prefs.edit { putBoolean(key, value) }

    actual fun clear() =
        prefs.edit { clear() }
}

actual fun createSettings(): AppSettings {
    throw UnsupportedOperationException("Use createSettings(context) on Android")
}

// Android ke liye context wala version
fun createSettings(context: Context): AppSettings =
    AppSettings(SettingsDelegate(context))