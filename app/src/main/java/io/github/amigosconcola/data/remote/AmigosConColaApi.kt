package io.github.amigosconcola.data.remote

import io.github.amigosconcola.data.dto.RefreshTokenDto
import io.github.amigosconcola.data.dto.TokensDto
import io.github.amigosconcola.data.dto.UserCredentialsDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AmigosConColaApi {
    @POST("api/auth/login")
    suspend fun login(@Body creds: UserCredentialsDto): TokensDto

    @POST("api/auth/refresh")
    suspend fun refresh(@Body refreshToken: RefreshTokenDto): TokensDto
}