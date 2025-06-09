package com.example.proba2.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba2.breeds.api.model.CatBreedApiModel
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.breeds.repository.CatBreedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatBreedsViewModel @Inject constructor(
    private val repository: CatBreedsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatBreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatBreedsListState.() -> CatBreedsListState) = _state.update(reducer)

    init {
        fetchAllBreeds()
    }

    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val breeds = repository.fetchAllBreeds()

                // 1. Prvo prikaži podatke bez slika
                val initialList = breeds.map { it.asBreedUiModel() }
                setState { copy(breeds = initialList) }

                // 2. Zatim asinhrono ažuriraj slike po jednu
                breeds.forEach { breed ->
                    launch {
                        try {
                            val imageUrl = repository.fetchBreedImage(breed.imageId)
                            val updatedBreed = breed.asBreedUiModel().copy(imageUrl = imageUrl)

                            // Ažuriraj samo jednu stavku u listi
                            setState {
                                copy(
                                    breeds = this.breeds.map {
                                        if (it.id == updatedBreed.id) updatedBreed else it
                                    }
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("fetchImage", "Failed to fetch image for ${breed.id}", e)
                        }
                    }
                }

            } catch (error: Exception) {
                Log.e("fetchAllBreeds", "Failed to fetch breeds", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun CatBreedApiModel.asBreedUiModel() = CatBreedUiModel(
        name = this.name,
        childFriendly = childFriendly,
        dogFriendly = dogFriendly,
        energyLevel = energyLevel,
        imageId = imageId,
        temperament = temperament,
        intelligence = intelligence,
        vocalisation = vocalisation,
        lifespan = lifeSpan,
        wikipediaUrl = wikipediaUrl,
        weight = weight,
        description = description,
        isRare = isRare,
        originCountries = origin,
        id = id,
        alternativeName = alternativeNames,
        imageUrl = imageUrl,
    )
}