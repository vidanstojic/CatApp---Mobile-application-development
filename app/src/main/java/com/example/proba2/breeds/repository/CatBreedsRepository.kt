package com.example.proba2.breeds.repository

import android.util.Log
import com.example.proba2.breeds.api.CatBreedApi
import com.example.proba2.breeds.api.model.CatBreedApiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class CatBreedsRepository @Inject constructor(
    private val catBreedApi: CatBreedApi,
    private val okHttpClient: OkHttpClient,
) {
    suspend fun fetchAllBreeds(): List<CatBreedApiModel> {
        return withContext(Dispatchers.IO) {
            catBreedApi.getAllCatBreeds()
        }
    }

    suspend fun fetchBreedImage(imageId: String?): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = catBreedApi.getSpecificImage(imageId ?: "")
                response.url // Use the URL from the response
            } catch (e: Exception) {
                "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg" // Return the default image URL if there's an error
            }
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