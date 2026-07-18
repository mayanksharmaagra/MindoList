package com.jrprofessor.mindolist

import android.app.Application
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.initialize
import com.jrprofessor.mindolist.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        initKoin(this)
        System.setProperty("kotlin-logging-to-android-native", "true")
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
        Log.d("TodoApplication", "Firebase App Check initialized with Debug provider")
    }
}