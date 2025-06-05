package com.example.proba2.photos.gallery

import com.example.proba2.photos.albums.model.AlbumUiModel

interface AlbumGalleryContract {
    data class AlbumGalleryUiState(
        val loading: Boolean = false,
        val albums: List<AlbumUiModel> = emptyList(),
    )
}