package com.example.proba2.breeds.repository

import android.util.Log
import com.example.proba2.breeds.api.CatBreedApi
import com.example.proba2.breeds.api.model.CatBreedApiModel
import com.example.proba2.breeds.api.model.toEntity
import com.example.proba2.data.base.CatBreedDao
import com.example.proba2.data.model.CatBreedEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
class CatBreedsRepository @Inject constructor(
    private val catBreedApi: CatBreedApi,
    private val dao: CatBreedDao
) {

    private val imageCache = mutableMapOf<String, String>()
    private val breedImagesCache = mutableMapOf<String, List<String>>()

    fun observeAllBreeds(): Flow<List<CatBreedEntity>> = dao.getAll()

    suspend fun refreshAllBreedsFromApi() = withContext(Dispatchers.IO) {
        val apiData = catBreedApi.getAllCatBreeds()
        val mapped = apiData.map { apiModel ->
            val imageUrl = try {
                fetchBreedImage(apiModel.imageId)
            } catch (_: Exception) {
                null
            }
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
