package io.github.amigosconcola.domain.use_case

import io.github.amigosconcola.domain.model.Tokens
import io.github.amigosconcola.domain.repository.AuthRepository

class Login(
    private val auth: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<Tokens> {
        return auth.login(username, password)
    }
}