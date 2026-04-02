package com.jrprofessor.mindolist

import android.app.Application
import com.jrprofessor.mindolist.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        initKoin(this)
        System.setProperty("kotlin-logging-to-android-native", "true")
    }
}