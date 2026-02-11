package com.jrprofessor.mindolist.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jrprofessor.mindolist.data.repository.FirebaseAuthRepositoryImpl
import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.usecase.CreateUserAccountUseCase
import com.jrprofessor.mindolist.domain.usecase.GetResendCooldownUseCase
import com.jrprofessor.mindolist.domain.usecase.SendOtpUseCase
import com.jrprofessor.mindolist.domain.usecase.VerifyOtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance().apply {
            setPersistenceEnabled(true)
        }
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseDatabase
    ): FirebaseAuthRepository {
        return FirebaseAuthRepositoryImpl(firebaseAuth, database)
    }
}