package com.jrprofessor.mindolist.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private object PrefKeys {
    const val IS_LOGGED_IN = "is_logged_in"
    const val THEME_MODE = "theme_mode"
    const val NOTIFICATIONS_ENABLED = "notifications_enabled"
    const val AI_EXTRACTION_ENABLED = "ai_extraction_enabled"
}

expect fun createSettings(): AppSettings

class AppSettings(private val delegate: SettingsDelegate) {

    private val _themeModeFlow = MutableStateFlow(delegate.getString(PrefKeys.THEME_MODE, "DARK"))
    val themeModeFlow: StateFlow<String> = _themeModeFlow.asStateFlow()

    private val _notificationsFlow = MutableStateFlow(delegate.getBoolean(PrefKeys.NOTIFICATIONS_ENABLED, true))
    val notificationsFlow: StateFlow<Boolean> = _notificationsFlow.asStateFlow()

    private val _aiExtractionFlow = MutableStateFlow(delegate.getBoolean(PrefKeys.AI_EXTRACTION_ENABLED, true))
    val aiExtractionFlow: StateFlow<Boolean> = _aiExtractionFlow.asStateFlow()

    var isLoggedIn: Boolean
        get() = delegate.getBoolean(PrefKeys.IS_LOGGED_IN, false)
        set(value) = delegate.putBoolean(PrefKeys.IS_LOGGED_IN, value)

    var themeMode: String
        get() = delegate.getString(PrefKeys.THEME_MODE, "DARK")
        set(value) {
            delegate.putString(PrefKeys.THEME_MODE, value)
            _themeModeFlow.value = value
        }

    var notificationsEnabled: Boolean
        get() = delegate.getBoolean(PrefKeys.NOTIFICATIONS_ENABLED, true)
        set(value) {
            delegate.putBoolean(PrefKeys.NOTIFICATIONS_ENABLED, value)
            _notificationsFlow.value = value
        }

    var aiExtractionEnabled: Boolean
        get() = delegate.getBoolean(PrefKeys.AI_EXTRACTION_ENABLED, true)
        set(value) {
            delegate.putBoolean(PrefKeys.AI_EXTRACTION_ENABLED, value)
            _aiExtractionFlow.value = value
        }

    fun clear() = delegate.clear()
}

// Platform-specific implementation delegate
expect class SettingsDelegate {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun getString(key: String, defaultValue: String): String
    fun putString(key: String, value: String)
    fun clear()
}