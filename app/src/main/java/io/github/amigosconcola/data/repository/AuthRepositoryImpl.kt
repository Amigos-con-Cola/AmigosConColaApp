package io.github.amigosconcola.data.repository

import io.github.amigosconcola.data.dto.UserCredentialsDto
import io.github.amigosconcola.data.remote.AmigosConColaApi
import io.github.amigosconcola.domain.model.Tokens
import io.github.amigosconcola.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AmigosConColaApi
) : AuthRepository {
    companion object {
        const val TAG = "AuthRepositoryImpl"
    }

    override suspend fun login(username: String, password: String): Result<Tokens> {
        val userCredentials = UserCredentialsDto(
            username,
            password
        )

        return try {
            val tokensDto = api.login(userCredentials)
            val tokens = Tokens(tokensDto.accessToken, tokensDto.refreshToken)
            Result.success(tokens)
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 401) {
                Result.failure(Exception("Usuario o contraseña incorrectos"))
            } else {
                Result.failure(Exception("Hubo un error al ingresar"))
            }
        }
    }
}