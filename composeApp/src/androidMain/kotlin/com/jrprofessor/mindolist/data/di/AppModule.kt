package com.jrprofessor.mindolist.data.di

import com.jrprofessor.mindolist.domain.repository.FirebaseAuthRepository
import com.jrprofessor.mindolist.domain.usecase.CreateUserAccountUseCase
import com.jrprofessor.mindolist.domain.usecase.GetResendCooldownUseCase
import com.jrprofessor.mindolist.domain.usecase.LoginWithEmailUseCase
import com.jrprofessor.mindolist.domain.usecase.SendOtpUseCase
import com.jrprofessor.mindolist.domain.usecase.VerifyOtpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideSendOtpUseCase(authRepository: FirebaseAuthRepository): SendOtpUseCase {
        return SendOtpUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideVerifyOtpUseCase(repository: FirebaseAuthRepository): VerifyOtpUseCase {
        return VerifyOtpUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCreateUserAccountUseCase(repository: FirebaseAuthRepository): CreateUserAccountUseCase {
        return CreateUserAccountUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetResendCooldownUseCase(repository: FirebaseAuthRepository): GetResendCooldownUseCase {
        return GetResendCooldownUseCase(repository)
    }
    @Provides
    @Singleton
    fun provideLoginWithEmailUseCase(repository: FirebaseAuthRepository): LoginWithEmailUseCase {
        return LoginWithEmailUseCase(repository)
    }



}