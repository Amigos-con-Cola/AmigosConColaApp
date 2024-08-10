package io.github.amigosconcola.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.amigosconcola.data.remote.AmigosConColaApi
import io.github.amigosconcola.data.repository.AnimalRepositoryImpl
import io.github.amigosconcola.domain.repository.AnimalRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnimalModule {

    @Provides
    @Singleton
    fun providesAnimalRepository(api: AmigosConColaApi): AnimalRepository {
        return AnimalRepositoryImpl(api)
    }

}