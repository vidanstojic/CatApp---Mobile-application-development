package com.example.proba2.breeds.repository

import android.util.Log
import com.example.proba2.breeds.api.CatBreedApi
import com.example.proba2.breeds.api.model.CatBreedApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class CatBreedsRepository @Inject constructor(
    private val catBreedApi: CatBreedApi,
    private val okHttpClient: OkHttpClient,
) {
    private val imageCache = mutableMapOf<String, String>()

    suspend fun fetchAllBreeds(): List<CatBreedApiModel> {
        return withContext(Dispatchers.IO) {
            catBreedApi.getAllCatBreeds()
        }
    }

    suspend fun fetchBreedImage(imageId: String?): String {
        return withContext(Dispatchers.IO) {
            imageCache.getOrPut(imageId.orEmpty()) {
                try {
                    val response = catBreedApi.getSpecificImage(imageId.orEmpty())
                    response.url
                } catch (e: retrofit2.HttpException) {
                    if (e.code() == 429) {
                        delay(1000L)
                        return@withContext fetchBreedImage(imageId)
                    } else {
                        "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
                    }
                } catch (e: Exception) {
                    "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
                }
            }
        }
    }

    suspend fun fetchBreedDetails(breedId: String): CatBreedApiModel {
        return withContext(Dispatchers.IO) {
            catBreedApi.getCatBreed(breedId)
        }
    }

    private val breedImagesCache = mutableMapOf<String, List<String>>()

    suspend fun getBreedImages(breedId: String, limit: Int = 10): List<String> {
        return breedImagesCache.getOrPut(breedId) {
            val list = try {
                catBreedApi.searchBreedImages(breedId, limit)
                    .map { it.url }
            } catch (e: Exception) {
                emptyList()
            }
            list
        }
    }
    suspend fun searchBreeds(query: String): List<CatBreedApiModel> {
        return withContext(Dispatchers.IO) {
            catBreedApi.searchBreeds(query)
        }
    }


    suspend fun directOkHttpUseGetExample() {
        val request = Request.Builder()
            .url("https://servis.raf.edu.rs/users")
            .get()
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()
    }

}