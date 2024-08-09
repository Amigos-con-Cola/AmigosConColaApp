package io.github.amigosconcola.domain.use_case

class ValidatePassword {
    operator fun invoke(password: String): Result<String> {
        return if (password.isNotBlank()) {
            Result.success(password)
        } else {
            Result.failure(Exception("Por favor, ingrese una contraseña"))
        }
    }
}