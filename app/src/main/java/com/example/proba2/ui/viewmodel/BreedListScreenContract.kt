package com.example.proba2.ui.viewmodel

import com.example.proba2.domain.Breed

interface BreedListScreenContract {

    data class UiState(
        val loading: Boolean = true,
        val data: List<Breed> = emptyList(),
        val error: Throwable? = null,
    )

    sealed class UiEvent {
        data object RefreshData : UiEvent()
    }

    sealed class SideEffect {
        // No side effects here
    }
}