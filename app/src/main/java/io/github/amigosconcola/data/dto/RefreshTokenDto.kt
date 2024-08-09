package io.github.amigosconcola.data.dto

import com.squareup.moshi.Json

data class RefreshTokenDto(
    @Json(name = "refresh_token") val refreshToken: String
)