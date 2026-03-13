package com.jrprofessor.mindolist.domain.di

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
import com.jrprofessor.mindolist.domain.usecase.SendOtpUseCase
import com.jrprofessor.mindolist.domain.usecase.VerifyOtpUseCase
import com.jrprofessor.mindolist.model.TaskCardData
import com.jrprofessor.mindolist.viewmodels.AddTaskViewModel
import com.jrprofessor.mindolist.viewmodels.AuthViewModel
import com.jrprofessor.mindolist.viewmodels.DashboardViewModel
import com.jrprofessor.mindolist.viewmodels.LoginViewModel
import com.jrprofessor.mindolist.viewmodels.SignUpViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


// ✅ ViewModel module
val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::AddTaskViewModel)
    viewModelOf(::AuthViewModel)
}


val useCaseModule = module {
    factoryOf(::SendOtpUseCase)
    factoryOf(::VerifyOtpUseCase)
    factoryOf(::CreateUserAccountUseCase)
    factoryOf(::GetResendCooldownUseCase)
    factoryOf(::LoginWithEmailUseCase)
    factoryOf(::AddTaskUseCase)
    factoryOf(::GetTasksUseCase)
}


val firebaseModule = module {

    // ✅ dev.gitlive Firebase Auth
    single {
        Firebase.auth
    }

    // ✅ dev.gitlive Firebase Database with persistence
    single {
        Firebase.database.apply {
            setPersistenceEnabled(true)
        }
    }

    // ✅ Repository binding
    single<FirebaseAuthRepository> {
        FirebaseAuthRepositoryImpl(
            firebaseAuth = get(),
            firebaseDatabase = get()
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
    viewModelModule,
    useCaseModule,
    firebaseModule
)