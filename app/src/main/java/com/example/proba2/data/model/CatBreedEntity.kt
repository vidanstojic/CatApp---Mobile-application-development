package com.example.proba2.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proba2.breeds.api.model.Weight

@Entity(tableName = "cat_breeds")
data class CatBreedEntity(
    @PrimaryKey val id: String,
    val name: String,
    val alternativeNames: String? = null,
    val description: String,
    val temperament: String,
    val origin: String,
    val countryCodes: String,
    val countryCode: String,
    val lifeSpan: String,
    @Embedded(prefix = "weight_imperial") val weight_impeliral: Weight,
    @Embedded(prefix = "weight_metric") val weight_metric: Weight,
    val childFriendly: Int,
    val dogFriendly: Int,
    val intelligence: Int,
    val energyLevel: Int,
    val vocalisation: Int,
    val isRare: Int,
    val wikipediaUrl: String?,
    val imageId: String?,
    val imageUrl: String?
)
