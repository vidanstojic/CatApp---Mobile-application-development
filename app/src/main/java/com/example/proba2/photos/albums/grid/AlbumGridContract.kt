package com.example.proba2.photos.albums.grid

import com.example.proba2.photos.albums.model.AlbumUiModel

interface AlbumGridContract {
    data class AlbumGridUiState(
        val loading: Boolean = false,
        val albums: List<AlbumUiModel> = emptyList(),
    )
}