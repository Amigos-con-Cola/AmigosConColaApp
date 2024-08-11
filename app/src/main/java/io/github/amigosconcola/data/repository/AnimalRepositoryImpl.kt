package io.github.amigosconcola.data.repository

import android.util.Log
import io.github.amigosconcola.data.dto.AnimalDto
import io.github.amigosconcola.data.dto.toDomain
import io.github.amigosconcola.data.remote.AmigosConColaApi
import io.github.amigosconcola.domain.model.Animal
import io.github.amigosconcola.domain.repository.AnimalRepository
import retrofit2.HttpException
import java.net.SocketTimeoutException

class AnimalRepositoryImpl(
    private val api: AmigosConColaApi
) : AnimalRepository {
    companion object {
        const val TAG = "AnimalRepositoryImpl"
    }

    override suspend fun getAnimals(page: Int, name: String?): List<Animal> {
        return try {
            val response = api.getAnimals(page, name)
            response.data
                .map(AnimalDto::toDomain)
                .map {
                    it.copy(
                        imagen = it.imagen?.replace(
                            "http://localhost:5130/",
                            AmigosConColaApi.API_BASE
                        )
                    )
                }
        } catch (ex: HttpException) {
            Log.d(TAG, "There was a problem fetching animals", ex)
            emptyList()
        } catch (ex: SocketTimeoutException) {
            Log.d(TAG, "The server is took too long to response")
            emptyList()
        }
    }
}