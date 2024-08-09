package io.github.amigosconcola.domain.repository

import io.github.amigosconcola.domain.model.Tokens

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<Tokens>
}