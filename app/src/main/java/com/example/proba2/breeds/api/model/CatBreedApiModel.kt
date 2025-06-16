package com.example.proba2.breeds.api.model

import com.example.proba2.data.model.CatBreedEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatBreedApiModel(
    val id: String,
    val name: String,
    @SerialName("alt_names")
    val alternativeNames: String? = null,
    val description: String,
    val temperament: String,
    val origin: String,
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

fun CatBreedApiModel.toEntity(): CatBreedEntity = CatBreedEntity(
    id = id,
    name = name,
    description = description,
    temperament = temperament,
    origin = origin,
    countryCodes = countryCodes,
    countryCode = countryCode,
    lifeSpan = lifeSpan,
    weight_impeliral = weight,
    weight_metric = weight,
    childFriendly = childFriendly,
    dogFriendly = dogFriendly,
    intelligence = intelligence,
    energyLevel = energyLevel,
    vocalisation = vocalisation,
    isRare = isRare,
    wikipediaUrl = wikipediaUrl,
    imageId = imageId,
    imageUrl = imageUrl
)

