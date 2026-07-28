package com.jrprofessor.mindolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jrprofessor.mindolist.domain.GoogleCalendarRepository
import com.jrprofessor.mindolist.domain.model.Result
import com.jrprofessor.mindolist.domain.model.User
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.domain.usecase.AddTaskUseCase
import com.jrprofessor.mindolist.domain.usecase.CreateUserAccountUseCase
import com.jrprofessor.mindolist.domain.usecase.GetResendCooldownUseCase
import com.jrprofessor.mindolist.domain.usecase.GetTasksUseCase
import com.jrprofessor.mindolist.domain.usecase.LoginWithEmailUseCase
import com.jrprofessor.mindolist.domain.usecase.SendForgotPasswordResetLink
import com.jrprofessor.mindolist.domain.usecase.SendOtpUseCase
import com.jrprofessor.mindolist.domain.usecase.VerifyOtpUseCase
import com.jrprofessor.mindolist.model.TaskModel
import com.jrprofessor.mindolist.screen.IntegrationsScreen
import com.jrprofessor.mindolist.theme.AppTheme
import com.jrprofessor.mindolist.utils.GoogleAuthManager
import com.jrprofessor.mindolist.utils.GoogleUserData
import com.jrprofessor.mindolist.viewmodels.AnalyticsViewModel
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.ForgotPasswordViewModel
import com.jrprofessor.mindolist.viewmodels.GoogleCalendarViewModel
import com.jrprofessor.mindolist.viewmodels.LoginViewModel
import com.jrprofessor.mindolist.viewmodels.SignUpViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate
import org.koin.compose.KoinApplication
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { false }
        setContent {
            App()
        }
    }
}

/**
 * A generic wrapper for previewing any screen that requires Koin dependencies.
 * Just call this in your preview and pass the screen you want to see.
 */
@Composable
fun ScreenPreview(content: @Composable () -> Unit) {
    KoinApplication(application = {
        modules(module {
            // Mocks for Repositories
            val mockAuthRepo = object : FirebaseAuthRepository {
                override suspend fun sendOtpToEmail(email: String) = Result.Success("12345")
                override suspend fun verifyOtp(email: String, otp: String) = Result.Success(true)
                override suspend fun createUserWithEmailAndPassword(
                    name: String,
                    email: String,
                    password: String,
                    profileUrl: String
                ) = Result.Success(User())

                override suspend fun saveUserToDatabase(user: User) = Result.Success(Unit)
                override suspend fun isOtpValid(email: String) = Result.Success(true)
                override suspend fun getResendCooldown(email: String) = Result.Success(0)
                override suspend fun loginWithEmailAndPassword(email: String, password: String) =
                    Result.Success(User())

                override suspend fun resetPassword(email: String, password: String) =
                    Result.Success(Unit)

                override fun getCurrentUser(): Flow<User?> = flowOf(User())
                override suspend fun signOut() = Result.Success(Unit)
                override fun isLoggedIn(): Boolean = true
                override fun authState(): Flow<Boolean> = flowOf(true)
                override suspend fun uploadProfileImage(
                    imageBytes: ByteArray,
                    email: String?
                ): Result<String> = Result.Success("https://example.com")

                override suspend fun sendPasswordResetEmail(email: String): Result<Unit> =
                    Result.Success(Unit)

                override suspend fun deleteAccount(): Result<Unit> = Result.Success(Unit)
            }

            val mockTaskRepo = object : TaskRepository {
                override fun getTasks() = flowOf(Result.Success(emptyList<TaskModel>()))
                override fun getTasksByDate(date: LocalDate) =
                    flowOf(Result.Success(emptyList<TaskModel>()))

                override suspend fun addTask(task: TaskModel) = Result.Success(Unit)
                override suspend fun updateTask(task: TaskModel) = Result.Success(Unit)
                override suspend fun markComplete(taskId: String, isCompleted: Boolean) =
                    Result.Success(Unit)

                override suspend fun deleteTask(taskId: String) = Result.Success(Unit)
                override fun getTasksInRange(startMillis: Long, endMillis: Long) =
                    flowOf(Result.Success(emptyList<TaskModel>()))
            }

            single<FirebaseAuthRepository> { mockAuthRepo }
            single<TaskRepository> { mockTaskRepo }

            single { GoogleCalendarRepository() }
            single<GoogleAuthManager> {
                object : GoogleAuthManager {
                    override val userData: StateFlow<GoogleUserData?> = MutableStateFlow(null).asStateFlow()
                    override fun signOut() {}
                }
            }

            // UseCases
            factoryOf(::SendOtpUseCase)
            factoryOf(::VerifyOtpUseCase)
            factoryOf(::CreateUserAccountUseCase)
            factoryOf(::GetResendCooldownUseCase)
            factoryOf(::LoginWithEmailUseCase)
            factoryOf(::AddTaskUseCase)
            factoryOf(::GetTasksUseCase)
            factoryOf(::SendForgotPasswordResetLink)

            // ViewModels
            viewModelOf(::LoginViewModel)
            viewModelOf(::SignUpViewModel)
            viewModelOf(::ForgotPasswordViewModel)
            viewModelOf(::DashboardViewModel)
            viewModelOf(::AnalyticsViewModel)
            viewModelOf(::GoogleCalendarViewModel)
        })
    }) {
        AppTheme {
            content()
        }
    }
}

@Preview(showSystemUi = true, name = "Welcome Screen Preview")
@Composable
fun WelcomePreview() {
    ScreenPreview {
//        WelcomeScreen(
//            onNavigateToHome = {
//
//            },
//            onNavigateToForgot = {
//
//            },
//            onNavigateToSignUpStep = {
//
//            })
//        MindoLogo(modifier = Modifier.size(200.dp))
        IntegrationsScreen(onBackClick = {

        })
    }
}
