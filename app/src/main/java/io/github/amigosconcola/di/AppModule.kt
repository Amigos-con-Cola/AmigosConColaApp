package io.github.amigosconcola.di

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.amigosconcola.data.remote.AmigosConColaApi
import io.github.amigosconcola.data.remote.AuthenticationInterceptor
import io.github.amigosconcola.data.repository.TokenRepositoryImpl
import io.github.amigosconcola.domain.repository.TokenRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    @Named("NonAuth")
    fun providesNonAuthAmigosConColaApi(): AmigosConColaApi {
        val moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AmigosConColaApi.API_BASE)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(AmigosConColaApi::class.java)
    }

    @Singleton
    @Provides
    fun providesAmigosConColaApi(
        tokens: TokenRepository,
        @Named("NonAuth") api: AmigosConColaApi,
    ): AmigosConColaApi {
        val moshi = Moshi.Builder()
            .add(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory())
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthenticationInterceptor(tokens, api))
            .authenticator(AuthenticationInterceptor(tokens, api))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(AmigosConColaApi.API_BASE)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(AmigosConColaApi::class.java)
    }

    @Singleton
    @Provides
    fun providesTokenRepository(
        @ApplicationContext context: Context
    ): TokenRepository {
        return TokenRepositoryImpl(context)
    }
}