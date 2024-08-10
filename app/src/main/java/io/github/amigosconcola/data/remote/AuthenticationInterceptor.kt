package io.github.amigosconcola.data.remote

import android.util.Log
import io.github.amigosconcola.data.dto.RefreshTokenDto
import io.github.amigosconcola.domain.model.Tokens
import io.github.amigosconcola.domain.repository.TokenRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.HttpException

class AuthenticationInterceptor(
    private val _tokens: TokenRepository,
    private val _api: AmigosConColaApi
) : Interceptor, Authenticator {
    companion object {
        const val TAG = "AuthenticationInterceptor"
    }

    init {
        runBlocking {
            _tokens.loadTokens()
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val tokens = _tokens.tokens().firstOrNull()
        var request = chain.request()
        if (tokens != null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer ${tokens.accessToken}")
                .build()
        }
        return@runBlocking chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        try {
            Log.d(TAG, "Refreshing access token")

            val tokens = _tokens.tokens().firstOrNull() ?: return@runBlocking null
            val newTokens = _api.refresh(RefreshTokenDto(tokens.refreshToken))

            _tokens.saveTokens(Tokens(newTokens.accessToken, newTokens.refreshToken))

            Log.d(TAG, "Access token refreshed")

            return@runBlocking response.request
                .newBuilder()
                .header("Authorization", "Bearer ${newTokens.accessToken}")
                .build()
        } catch (ex: HttpException) {
            Log.d(TAG, "Failed to refresh token: ${ex.message()}")
            _tokens.deleteTokens()
            return@runBlocking null
        }
    }
}
