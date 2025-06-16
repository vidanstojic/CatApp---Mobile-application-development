package com.example.proba2.breeds.list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba2.breeds.api.model.CatBreedApiModel
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.breeds.repository.CatBreedsRepository
import com.example.proba2.data.model.CatBreedEntity
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
        viewModelScope.launch {
            repository.refreshAllBreedsFromApi() // Prvi i jedini poziv ka API-ju
            observeBreedsFromDb() // Nakon toga slušamo bazu
        }
    }

    private fun observeBreedsFromDb() {
        viewModelScope.launch {
            repository.observeAllBreeds().collect { entities ->
                val uiModels = entities.map { it.toUiModel() }
                setState { copy(breeds = uiModels) }
            }
        }
    }

//    private fun fetchAllBreeds() {
//        viewModelScope.launch {
//            setState { copy(loading = true) }
//            try {
//                val breeds = repository.fetchAllBreeds()
//
//                val initialList = breeds.map { it.asBreedUiModel() }
//                setState { copy(breeds = initialList) }
//
//                val semaphore = Semaphore(permits = 5) // Dozvoljeno max 5 paralelnih zahteva
//
//                breeds.forEach { breed ->
//                    launch {
//                        semaphore.withPermit {
//                            try {
//                                val imageUrl = repository.fetchBreedImage(breed.imageId)
//                                val updatedBreed = breed.asBreedUiModel().copy(imageUrl = imageUrl)
//
//                                setState {
//                                    copy(
//                                        breeds = this.breeds.map {
//                                            if (it.id == updatedBreed.id) updatedBreed else it
//                                        }
//                                    )
//                                }
//                            } catch (e: Exception) {
//                                // Logovanje ako zatreba
//                            }
//                        }
//                    }
//                }
//
//            } catch (error: Exception) {
//                // Log error
//            } finally {
//                setState { copy(loading = false) }
//            }
//        }
//    }

    private var _selectedBreed = MutableStateFlow<CatBreedUiModel?>(null)
    val selectedBreed = _selectedBreed.asStateFlow()

    fun fetchBreedDetails(breedId: String) {
        viewModelScope.launch {
            cachedBreedDetails[breedId]?.let {
                _selectedBreed.value = it
                return@launch
            }

            try {
                val entity = repository.getBreedDetailsFromDb(breedId)
                val uiModel = entity.toUiModel()
                cachedBreedDetails[breedId] = uiModel
                _selectedBreed.value = uiModel
            } catch (e: Exception) {
                Log.e("FetchBreedDetails", "Failed to load from DB", e)
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
/**
 *
 *     private val _state = MutableStateFlow(CatBreedsListState())
 *     val state = _state.asStateFlow()
 *     private fun setState(reducer: CatBreedsListState.() -> CatBreedsListState) = _state.update(reducer)
 *
 * */

    private val _searchState = MutableStateFlow(CatBreedsListState())
    val results = _searchState.asStateFlow()
    private fun setSearchState(reducer: CatBreedsListState.() -> CatBreedsListState) = _searchState.update(reducer)

    fun search(query: String) {
        viewModelScope.launch {
            setSearchState { copy(loading = true) }
            try {
                val searchResults = repository.searchBreedsFromDb(query)
                val uiModels = searchResults.map { it.toUiModel() }
                setSearchState { copy(breeds = uiModels) }
            } catch (e: Exception) {
                Log.e("Search", "Search failed", e)
            } finally {
                setSearchState { copy(loading = false) }
            }
        }
    }


    fun CatBreedEntity.toUiModel() = CatBreedUiModel(
        name = name,
        childFriendly = childFriendly,
        dogFriendly = dogFriendly,
        energyLevel = energyLevel,
        imageId = imageId,
        temperament = temperament,
        intelligence = intelligence,
        vocalisation = vocalisation,
        lifespan = lifeSpan,
        wikipediaUrl = wikipediaUrl,
        weight = weight_metric,
        description = description,
        isRare = isRare,
        originCountries = origin,
        id = id,
        alternativeName = alternativeNames,
        imageUrl = imageUrl,
    )

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