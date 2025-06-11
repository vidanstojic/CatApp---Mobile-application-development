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
                setState {
                    copy(
                        breeds = initialList,
                        filteredBreeds = initialList
                    )
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
            // Ako već imamo detalje u kešu — koristi ih odmah
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

    private val _breedImage = MutableStateFlow<String?>(null)
    val breedImage = _breedImage.asStateFlow()

    // Method to fetch breed image by imageId
    fun fetchBreedImage(imageId: String?) {
        viewModelScope.launch {
            if (imageId != null && imageId.isNotBlank()) {
                try {
                    val imageUrl = repository.fetchBreedImage(imageId)
                    _breedImage.value = imageUrl
                } catch (e: Exception) {
                    Log.e("FetchBreedImage", "Failed to fetch breed image", e)
                    _breedImage.value = "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg" // Default image URL
                }
            }
        }
    }

    var searchQuery by mutableStateOf("")
        private set

    fun updateSearchQuery(query: String) {
        searchQuery = query
        filterBreeds()
    }

    private fun filterBreeds() {
        val allBreeds = state.value.breeds
        if (searchQuery.isBlank()) {
            _state.update { it.copy(filteredBreeds = allBreeds) }
        } else {
            val filtered = allBreeds.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
            _state.update { it.copy(filteredBreeds = filtered) }
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