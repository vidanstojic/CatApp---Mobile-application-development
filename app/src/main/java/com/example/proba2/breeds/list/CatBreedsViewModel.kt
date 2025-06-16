package com.example.proba2.breeds.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba2.breeds.api.model.CatBreedApiModel
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.breeds.repository.CatBreedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatBreedsViewModel @Inject constructor(
    private val repository: CatBreedsRepository,
) : ViewModel() {

    private val cachedBreedDetails = mutableMapOf<String, CatBreedUiModel>()
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

                val initialList = breeds.map { it.asBreedUiModel() }
                setState { copy(breeds = initialList) }

                breeds.forEach { breed ->
                    launch {
                        try {
                            val imageUrl = repository.fetchBreedImage(breed.imageId)
                            val updatedBreed = breed.asBreedUiModel().copy(imageUrl = imageUrl)

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
    private var _selectedBreed = MutableStateFlow<CatBreedUiModel?>(null)
    val selectedBreed = _selectedBreed.asStateFlow()

    fun fetchBreedDetails(breedId: String) {
        viewModelScope.launch {
            cachedBreedDetails[breedId]?.let {
                _selectedBreed.value = it
                return@launch
            }

            try {
                val breedDetails = repository.fetchBreedDetails(breedId)
                val uiModel = breedDetails.asBreedUiModel()
                cachedBreedDetails[breedId] = uiModel
                _selectedBreed.value = uiModel
            } catch (e: Exception) {
                Log.e("FetchBreedDetails", "Failed to fetch breed details", e)
            }
        }
    }
    private val _breedImages = MutableStateFlow<List<String>>(emptyList())
    val breedImages: StateFlow<List<String>> = _breedImages

    fun loadBreedImages(breedId: String, count: Int = 10) {
        viewModelScope.launch {
            val images = repository.getBreedImages(breedId, count)
            _breedImages.value = images
        }
    }

    private val _searchState = MutableStateFlow(CatBreedsListState())
    val results = _searchState.asStateFlow()
    private fun setSearchState(reducer: CatBreedsListState.() -> CatBreedsListState) = _searchState.update(reducer)

    fun search(query: String) {
        viewModelScope.launch {
            setSearchState { copy(loading = true) }
            try {
                val breeds = repository.searchBreeds(query)

                val initialList = breeds.map { it.asBreedUiModel() }
                setSearchState { copy(breeds = initialList) }

                breeds.forEach { breed ->
                    launch {
                        try {
                            val imageUrl = repository.fetchBreedImage(breed.imageId)
                            val updatedBreed = breed.asBreedUiModel().copy(imageUrl = imageUrl)

                            setSearchState {
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