package com.example.proba2.breeds.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatBreedApiModel(
    val id: String,
    val name: String,
    @SerialName("alt_names")
    val alternativeNames: String? = null,//proveriti da li treba lista
    val description: String,
    val temperament: String,//proveriti da li treba lista
    val origin: String,//proveriti da li treba lista
    @SerialName("country_codes")
    val countryCodes: String,
    @SerialName("country_code")
    val countryCode: String,
    @SerialName("life_span")
    val lifeSpan: String,
    val weight: Weight,
    @SerialName("child_friendly")
    val childFriendly: Int,
    @SerialName("dog_friendly")
    val dogFriendly: Int,
    val intelligence: Int,
    @SerialName("energy_level")
    val energyLevel: Int,
    val vocalisation: Int,
    @SerialName("rare")
    val isRare: Int,
    @SerialName("wikipedia_url")
    val wikipediaUrl: String? = null,
    @SerialName("reference_image_id")
    val imageId: String? = null,
    var imageUrl: String? = null,
)

@Serializable
data class CatImageResponse(
    val id: String,
    @SerialName("url")
    val url: String,
)

@Serializable
data class Weight(
    val imperial: String,
    val metric: String,
)
