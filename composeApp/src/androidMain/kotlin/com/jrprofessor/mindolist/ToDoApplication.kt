package com.jrprofessor.mindolist

import android.app.Application
import com.jrprofessor.mindolist.di.initKoin

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}