package com.example.proba2.breeds.repository

import android.content.Context
import com.example.proba2.breeds.api.CatBreedApi
import com.example.proba2.breeds.api.model.toEntity
import com.example.proba2.data.base.CatBreedDao
import com.example.proba2.data.model.CatBreedEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
class CatBreedsRepository @Inject constructor(
    private val catBreedApi: CatBreedApi,
    private val dao: CatBreedDao,
    @ApplicationContext private val context: Context,
) {

    private val imageCache = mutableMapOf<String, String>()
    private val breedImagesCache = mutableMapOf<String, List<String>>()

    fun observeAllBreeds(): Flow<List<CatBreedEntity>> = dao.getAll()

    suspend fun refreshAllBreedsFromApi() = withContext(Dispatchers.IO) {
        val apiData = catBreedApi.getAllCatBreeds()
        val mapped = apiData.map { apiModel ->
            // Avoid N+1 image requests on app startup. Build direct CDN URL from reference id.
            val imageUrl = apiModel.imageId?.let { "https://cdn2.thecatapi.com/images/$it.jpg" }
            apiModel.toEntity().copy(imageUrl = imageUrl)
        }
        dao.clearAll()
        dao.insertAll(mapped)
    }

    suspend fun searchBreedsFromDb(query: String): List<CatBreedEntity> {
        return dao.search("%$query%")
    }

    suspend fun fetchBreedImage(imageId: String?): String {
        return imageCache.getOrPut(imageId.orEmpty()) {
            try {
                val response = catBreedApi.getSpecificImage(imageId.orEmpty())
                response.url
            } catch (e: Exception) {
                "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
            }
        }
    }

    suspend fun getBreedDetailsFromDb(breedId: String): CatBreedEntity {
        return dao.getById(breedId)
    }

    suspend fun getBreedImages(breedId: String, limit: Int = 10): List<String> {
        return breedImagesCache.getOrPut(breedId) {
            try {
                catBreedApi.searchBreedImages(breedId, limit).map { it.url }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}
