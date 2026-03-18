package com.jrprofessor.mindolist.local

private object PrefKeys {
    const val IS_LOGGED_IN = "is_logged_in"
}

expect fun createSettings(): AppSettings

class AppSettings(private val delegate: SettingsDelegate) {

    var isLoggedIn: Boolean
        get() = delegate.getBoolean(PrefKeys.IS_LOGGED_IN, false)
        set(value) = delegate.putBoolean(PrefKeys.IS_LOGGED_IN, value)

    fun clear() = delegate.clear()
}

// Platform-specific implementation delegate
expect class SettingsDelegate {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun putBoolean(key: String, value: Boolean)
    fun clear()
}