package com.example.proba2.photos.repository

import com.example.proba2.photos.api.CatImagesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CatImagesRepository @Inject constructor(
    private val photosApi: CatImagesApi,
) {

    suspend fun getAlbumPhotos(albumId: Int) = photosApi.getAlbumPhotos(albumId = albumId)

    suspend fun getUserAlbums(userId: Int) = withContext(Dispatchers.IO) {
        photosApi.getAlbums(userId = userId)
    }

    suspend fun deleteAlbum(albumId: Int) = photosApi.deleteAlbum(albumId = albumId)

}