package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.local.AppSettings
import com.jrprofessor.mindolist.local.SettingsDelegate
import com.jrprofessor.mindolist.utils.IosNetworkConnectivityManager
import com.jrprofessor.mindolist.utils.IosNotificationScheduler
import com.jrprofessor.mindolist.utils.IosSpeechToTextParser
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.utils.SpeechToTextParser
import com.jrprofessor.mindolist.utils.NetworkConnectivityManager
import com.jrprofessor.mindolist.utils.NotificationScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.dsl.module
import org.koin.core.module.Module
import kotlin.coroutines.resume

class IosGoogleAuthManager : GoogleAuthManager {
    private val _userData = MutableStateFlow<User?>(null)
    override val userData: StateFlow<User?> = _userData.asStateFlow()
    
    override fun signOut() {
        _userData.value = null
    }
    
    override suspend fun refreshAccessToken(oldToken: String?): String? = suspendCancellableCoroutine { continuation ->
        PlatformBridgeHolder.googleSignInBridge.refreshAccessToken(
            onSuccess = { newToken ->
                _userData.update { it?.copy(googleAccessToken = newToken) }
                continuation.resume(newToken)
            },
            onError = {
                continuation.resume(null)
            }
        )
    }
    
    fun updateUserData(user: User?) {
        _userData.update { user }
    }
}

actual val platformModule: Module = module {
    single { SettingsDelegate() }
    single { AppSettings(get()) }
    single<SpeechToTextParser> { IosSpeechToTextParser() }
    single<GoogleAuthManager> { IosGoogleAuthManager() }
    single<NetworkConnectivityManager> { IosNetworkConnectivityManager() }
    single<NotificationScheduler> { IosNotificationScheduler() }
}
