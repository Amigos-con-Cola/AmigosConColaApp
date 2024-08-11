package io.github.amigosconcola.domain.repository

import io.github.amigosconcola.domain.model.Animal

interface AnimalRepository {
    suspend fun getAnimals(page: Int = 1, name: String? = null): List<Animal>
}