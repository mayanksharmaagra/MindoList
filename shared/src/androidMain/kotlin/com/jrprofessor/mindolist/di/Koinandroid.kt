package com.jrprofessor.mindolist.di

import android.content.Context
import com.jrprofessor.mindolist.domain.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        androidLogger()
        modules(appModules())
    }
}