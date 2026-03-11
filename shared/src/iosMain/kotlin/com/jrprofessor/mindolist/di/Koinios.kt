package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.domain.di.appModules

import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(appModules())
    }
}