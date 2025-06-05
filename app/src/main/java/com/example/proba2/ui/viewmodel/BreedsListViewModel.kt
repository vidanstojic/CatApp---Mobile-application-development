package com.example.proba2.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba2.breeds.api.model.CatBreedApiModel
import com.example.proba2.breeds.list.CatBreedsListState
import com.example.proba2.breeds.list.model.CatBreedUiModel
import com.example.proba2.domain.BreedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.update

@HiltViewModel
class BreedsListViewModel @Inject constructor(
    private val repository: BreedRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CatBreedsListState())
    val state = _state.asStateFlow()
    private fun setState(reducer: CatBreedsListState.() -> CatBreedsListState) = _state.update(reducer)

    init {
        fetchAllUsers()
    }

    private fun fetchAllUsers() {
        viewModelScope.launch {
            setState { copy(loading = true) }
            try {
                val breeds = repository.fetchAllBreeds().map { it.asBreedUiModel() }
                setState { copy(breeds = breeds) }
            } catch (error: Exception) {
                // TODO Handle error
                Log.d("test", "Failed to fetch.", error)
            } finally {
                setState { copy(loading = false) }
            }
        }
    }

    private fun CatBreedApiModel.asBreedUiModel() = CatBreedUiModel(
        id = this.id,
        name = this.name,
        isRare = isRare,
        lifespan = lifeSpan,
        weight = weight,
        temperament = temperament,
        wikipediaUrl = wikipediaUrl,
        description = description,
        energyLevel = energyLevel,
        childFriendly = childFriendly,
        dogFriendly = dogFriendly,
        intelligence = intelligence,
        alternativeName = alternativeNames,
        vocalisation = vocalisation,
        originCountries = origin,
        imageUrl = imageUrl

    )
}
