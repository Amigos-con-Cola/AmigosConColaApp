package io.github.amigosconcola.domain.repository

import io.github.amigosconcola.domain.model.Tokens
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun tokens(): Flow<Tokens?>
    suspend fun loadTokens()
    suspend fun saveTokens(tokens: Tokens)
    suspend fun deleteTokens()
}