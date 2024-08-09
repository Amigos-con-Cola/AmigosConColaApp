package io.github.amigosconcola.domain.use_case

class ValidateUsername {
    operator fun invoke(username: String): Result<String> {
        return if (username.isNotBlank()) {
            Result.success(username)
        } else {
            Result.failure(Exception("Por favor, ingrese su nombre de usuario"))
        }
    }
}