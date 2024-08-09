package io.github.amigosconcola.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.amigosconcola.data.remote.AmigosConColaApi
import io.github.amigosconcola.data.repository.AuthRepositoryImpl
import io.github.amigosconcola.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun providesAuthRepository(
        api: AmigosConColaApi
    ): AuthRepository {
        return AuthRepositoryImpl(api)
    }
}