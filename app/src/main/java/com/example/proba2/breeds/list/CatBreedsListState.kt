package com.example.proba2.breeds.list

import com.example.proba2.breeds.list.model.CatBreedUiModel

data class CatBreedsListState(
    val loading: Boolean = false,
    val breeds: List<CatBreedUiModel> = emptyList(),
    )