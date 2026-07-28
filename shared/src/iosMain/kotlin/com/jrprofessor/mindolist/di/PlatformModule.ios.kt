package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.domain.repository.AiTaskRepository
import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.local.SettingsDelegate
import com.jrprofessor.mindolist.utils.IosSpeechToTextParser
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import com.jrprofessor.mindolist.utils.GoogleUserData
import com.jrprofessor.mindolist.utils.SpeechToTextParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.dsl.module
import org.koin.core.module.Module

class IosGoogleAuthManager : GoogleAuthManager {
    override val userData: StateFlow<GoogleUserData?> = MutableStateFlow(null)
    override fun signOut() {}
}

actual val platformModule: Module = module {
    single { SettingsDelegate() }
    single { AppSettings(get()) }
    single<SpeechToTextParser> { IosSpeechToTextParser() }
    single<GoogleAuthManager> { IosGoogleAuthManager() }
}
