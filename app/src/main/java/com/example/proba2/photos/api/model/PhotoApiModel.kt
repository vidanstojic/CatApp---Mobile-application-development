package com.example.proba2.photos.api.model

import com.example.proba2.breeds.api.model.CatBreedApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PhotoApiModel(
    @SerialName("id") val imageId: String,
    val url: String,
    val width: Int,
    val height: Int,
    val breeds: List<CatBreedApiModel>,
)