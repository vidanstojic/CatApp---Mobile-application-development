package com.example.proba2.breeds.repository

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

    suspend fun directOkHttpUseGetExample() {
        val request = Request.Builder()
            .url("https://servis.raf.edu.rs/users")
            .get()
            .build()
        val call = okHttpClient.newCall(request)
        val response = call.execute()
    }

}