package io.github.amigosconcola.data.dto

import com.squareup.moshi.Json

data class TokensDto(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "refresh_token") val refreshToken: String
)
