package io.github.amigosconcola.data.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.amigosconcola.domain.model.Tokens
import io.github.amigosconcola.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TokenRepositoryImpl(
    @ApplicationContext val context: Context
) : TokenRepository {
    companion object {
        private const val TAG = "TokenRepositoryImpl"
        private const val NAME = "amigosconcola"
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }

    private val _flow = MutableStateFlow<Tokens?>(null)
    override fun tokens(): Flow<Tokens?> = _flow

    override suspend fun loadTokens() {
        val sharedPrefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        val accessToken = sharedPrefs.getString(ACCESS_TOKEN_KEY, null) ?: return
        val refreshToken = sharedPrefs.getString(REFRESH_TOKEN_KEY, null) ?: return
        val tokens = Tokens(accessToken, refreshToken)
        _flow.update {
            Log.d(TAG, "Loaded tokens")
            tokens
        }
    }

    override suspend fun saveTokens(tokens: Tokens) {
        val sharedPrefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        val ok = sharedPrefs.edit()
            .putString(ACCESS_TOKEN_KEY, tokens.accessToken)
            .putString(REFRESH_TOKEN_KEY, tokens.refreshToken)
            .commit()
        if (ok) {
            Log.d(TAG, "Token saved")
            _flow.update { tokens }
        }
    }

    override suspend fun deleteTokens() {
        val sharedPrefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        val ok = sharedPrefs.edit()
            .putString(ACCESS_TOKEN_KEY, null)
            .putString(REFRESH_TOKEN_KEY, null)
            .commit()
        if (ok) {
            Log.d(TAG, "Token deleted")
            _flow.update { null }
        }
    }
}