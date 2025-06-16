package com.example.proba2.breeds.list.model

import com.example.proba2.breeds.api.model.Weight

data class CatBreedUiModel (
    val id: String,
    val name: String,
    val alternativeName: String?,
    val description: String,
    val temperament: String,
    val imageId: String?,
    val imageUrl: String?,
    val originCountries: String,
    val lifespan: String,
    val weight: Weight,
    val childFriendly: Int,
    val dogFriendly: Int,
    val intelligence: Int,
    val energyLevel: Int,
    val vocalisation: Int,
    val isRare: Int,
    val wikipediaUrl: String?,
    )