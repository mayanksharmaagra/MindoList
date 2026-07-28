package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.domain.repository.AiTaskRepository
import com.jrprofessor.mindolist.domain.repository.AiTaskRepositoryImpl
import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.local.SettingsDelegate
import com.jrprofessor.mindolist.utils.AndroidSpeechToTextParser
import com.jrprofessor.mindolist.utils.AndroidGoogleAuthManager
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import com.jrprofessor.mindolist.utils.SpeechToTextParser
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.Module


actual val platformModule: Module = module {
    single { SettingsDelegate(androidContext()) }
    single { AppSettings(get()) }
    single<AiTaskRepository> { AiTaskRepositoryImpl() }
    single<SpeechToTextParser> { AndroidSpeechToTextParser(androidContext()) }
    single<GoogleAuthManager> { AndroidGoogleAuthManager(androidContext()) }
}
