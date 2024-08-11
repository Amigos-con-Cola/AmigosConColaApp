package io.github.amigosconcola.data.remote

import io.github.amigosconcola.data.dto.AnimalDto
import io.github.amigosconcola.data.dto.PaginatedDto
import io.github.amigosconcola.data.dto.RefreshTokenDto
import io.github.amigosconcola.data.dto.TokensDto
import io.github.amigosconcola.data.dto.UserCredentialsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AmigosConColaApi {
    companion object {
        const val API_BASE = "https://amigosconcolaapi-production.up.railway.app"
    }

    @POST("api/auth/login")
    suspend fun login(@Body creds: UserCredentialsDto): TokensDto

    @POST("api/auth/refresh")
    suspend fun refresh(@Body refreshToken: RefreshTokenDto): TokensDto

    @GET("api/animals")
    suspend fun getAnimals(
        @Query("page") page: Int,
        @Query("name") name: String? = null
    ): PaginatedDto<AnimalDto>
}