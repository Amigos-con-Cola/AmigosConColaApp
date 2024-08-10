package io.github.amigosconcola.data.dto

import io.github.amigosconcola.domain.model.Animal

data class AnimalDto(
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

fun AnimalDto.toDomain(): Animal = Animal(
    id = id,
    nombre = nombre,
    edad = edad,
    genero = genero,
    imagen = imagen,
    adoptado = adoptado,
    especie = especie,
    historia = historia,
    ubicacion = ubicacion,
    peso = peso,
    codigo = codigo
)