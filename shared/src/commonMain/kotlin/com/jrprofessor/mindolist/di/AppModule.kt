package com.jrprofessor.mindolist.di

import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepositoryImpl
import com.jrprofessor.mindolist.domain.repository.TaskRepository
import com.jrprofessor.mindolist.domain.repository.TaskRepositoryImpl
import com.jrprofessor.mindolist.domain.usecase.AddTaskUseCase
//import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepositoryImpl
import com.jrprofessor.mindolist.domain.usecase.CreateUserAccountUseCase
import com.jrprofessor.mindolist.domain.usecase.GetResendCooldownUseCase
import com.jrprofessor.mindolist.domain.usecase.GetTasksUseCase
import com.jrprofessor.mindolist.domain.usecase.LoginWithEmailUseCase
import com.jrprofessor.mindolist.domain.usecase.SendForgotPasswordResetLink
import com.jrprofessor.mindolist.domain.usecase.SendOtpUseCase
import com.jrprofessor.mindolist.domain.usecase.VerifyOtpUseCase
import com.jrprofessor.mindolist.viewmodels.AnalyticsViewModel
import com.jrprofessor.mindolist.viewmodels.AuthViewModel
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.EditProfileViewModel
import com.jrprofessor.mindolist.viewmodels.ForgotPasswordViewModel
import com.jrprofessor.mindolist.viewmodels.LoginViewModel
import com.jrprofessor.mindolist.viewmodels.SettingsViewmodel
import com.jrprofessor.mindolist.viewmodels.SignUpViewModel
import com.jrprofessor.mindolist.viewmodels.TaskViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import dev.gitlive.firebase.storage.storage
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


// ✅ ViewModel module
val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::ForgotPasswordViewModel)
    viewModelOf(::DashboardViewModel)
    viewModel { TaskViewModel(get(), getOrNull(), get()) }
    viewModelOf(::AuthViewModel)
    viewModelOf(::SettingsViewmodel)
    viewModelOf(::EditProfileViewModel)
    viewModelOf(::AnalyticsViewModel)
}


val useCaseModule = module {
    factoryOf(::SendOtpUseCase)
    factoryOf(::VerifyOtpUseCase)
    factoryOf(::CreateUserAccountUseCase)
    factoryOf(::GetResendCooldownUseCase)
    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::AddTaskUseCase)
    factoryOf(::GetTasksUseCase)
    factoryOf(::SendForgotPasswordResetLink)
}


val firebaseModule = module {

    // ✅ dev.gitlive Firebase Auth
    single(createdAtStart = false) {
        Firebase.auth
    }

    // ✅ dev.gitlive Firebase Database with persistence
    single(createdAtStart = false) {
        Firebase.database.apply {
            setPersistenceEnabled(true)
        }
    }
    // ✅ dev.gitlive Firebase storage with persistence
    single(createdAtStart = false) {
        Firebase.storage
    }

    // ✅ Repository binding
    single<FirebaseAuthRepository> {
        FirebaseAuthRepositoryImpl(
            firebaseAuth = get(),
            firebaseDatabase = get(),
            firebaseStorage = get (),
            appSettings = get(),
        )
    }
    single<TaskRepository> {
        TaskRepositoryImpl(
            firebaseAuth = get(),
            firebaseDatabase = get()
        )
    }
}


// ✅ Sab modules ek jagah
fun appModules() = listOf(
    platformModule,
    firebaseModule,
    useCaseModule,
    viewModelModule
)

