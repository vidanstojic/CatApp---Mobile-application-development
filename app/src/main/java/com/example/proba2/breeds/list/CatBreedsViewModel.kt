package com.example.proba2.breeds.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba2.breeds.api.model.CatBreedApiModel
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.breeds.repository.CatBreedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
                val breeds = repository.fetchAllBreeds().map { breedApiModel ->
                    val imageUrl = repository.fetchBreedImage(breedApiModel.imageId)
                    breedApiModel.asBreedUiModel().copy(imageUrl = imageUrl)
                }
                setState { copy(breeds = breeds) }
            } catch (error: Exception) {
                Log.d("test", "Failed to fetch.", error)
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