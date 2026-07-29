package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.domain.repository.AiTaskRepository
import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.local.SettingsDelegate
import com.jrprofessor.mindolist.utils.IosSpeechToTextParser
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.utils.SpeechToTextParser
import com.jrprofessor.mindolist.utils.NetworkConnectivityManager
import com.jrprofessor.mindolist.utils.IosNetworkConnectivityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.dsl.module
import org.koin.core.module.Module

class IosGoogleAuthManager : GoogleAuthManager {
    override val userData: StateFlow<User?> = MutableStateFlow(null)
    override fun signOut() {}
    override suspend fun refreshAccessToken(oldToken: String?): String? = null
}

actual val platformModule: Module = module {
    single { SettingsDelegate() }
    single { AppSettings(get()) }
    single<SpeechToTextParser> { IosSpeechToTextParser() }
    single<GoogleAuthManager> { IosGoogleAuthManager() }
    single<NetworkConnectivityManager> { IosNetworkConnectivityManager() }
}
