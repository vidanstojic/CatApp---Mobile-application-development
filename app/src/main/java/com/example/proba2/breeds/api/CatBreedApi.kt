package com.example.proba2.breeds.api

import com.example.proba2.breeds.api.model.CatBreedApiModel
import com.example.proba2.breeds.api.model.CatImageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CatBreedApi {

    @GET("breeds")
    suspend fun getAllCatBreeds(): List<CatBreedApiModel>

    @GET("breeds/{breed_id}")
    suspend fun getCatBreed(
        @Path("breed_id") catName: String,
    ): CatBreedApiModel

    @POST("test/123")
    suspend fun test(
        @Body body: String,
        @Header("CustomHeader") test: String,
    )
    @GET("images/{image_id}")
    suspend fun getSpecificImage(
        @Path("image_id") imageId: String,
    ): CatImageResponse

    @GET("breeds/search?q=query")
    suspend fun search(
        @Path("query") id: String,
    ):String
}