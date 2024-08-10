package io.github.amigosconcola.domain.model

data class Animal(
    val id: Int,
    val nombre: String,
    val edad: Int,
    val genero: String,
    val imagen: String?,
    val adoptado: Boolean,
    val especie: String,
    val historia: String?,
    val ubicacion: String,
    val peso: Int,
    val codigo: String?
)
