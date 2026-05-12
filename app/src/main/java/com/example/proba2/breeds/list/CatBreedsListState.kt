package com.example.proba2.breeds.list

import com.example.proba2.breeds.list.model.CatBreedUiModel

data class CatBreedsListState(
    val breeds: List<CatBreedUiModel> = emptyList(),
    val filteredBreeds: List<CatBreedUiModel> = emptyList(),
    val loading: Boolean = false,
)