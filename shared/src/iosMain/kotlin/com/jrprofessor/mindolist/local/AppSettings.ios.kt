package com.jrprofessor.mindolist.local

import platform.Foundation.NSUserDefaults

actual class SettingsDelegate {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        if (defaults.objectForKey(key) != null)
            defaults.boolForKey(key)
        else defaultValue

    actual fun putBoolean(key: String, value: Boolean) =
        defaults.setBool(value, forKey = key)

    actual fun clear() =
        defaults.dictionaryRepresentation().keys.forEach {
            defaults.removeObjectForKey(it as String)
        }
}

actual fun createSettings(): AppSettings =
    AppSettings(SettingsDelegate())