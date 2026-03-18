package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.local.SettingsDelegate
import org.koin.dsl.module
import org.koin.core.module.Module


actual val platformModule: Module = module {
    single { SettingsDelegate() }
    single { AppSettings(get()) }
}