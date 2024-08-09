package io.github.amigosconcola.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.amigosconcola.domain.repository.AuthRepository
import io.github.amigosconcola.domain.use_case.Login
import io.github.amigosconcola.domain.use_case.ValidatePassword
import io.github.amigosconcola.domain.use_case.ValidateUsername
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    @Singleton
    @Provides
    fun providesValidateUsernameUseCase(): ValidateUsername {
        return ValidateUsername()
    }

    @Singleton
    @Provides
    fun providesValidatePasswordUseCase(): ValidatePassword {
        return ValidatePassword()
    }

    @Singleton
    @Provides
    fun providesLoginUseCase(auth: AuthRepository): Login {
        return Login(auth)
    }
}