package com.example.proba2.photos.di

import com.example.proba2.photos.api.CatImagesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PhotosModule {

    @Provides
    @Singleton
    fun providePhotosApi(retrofit: Retrofit) = retrofit.create(CatImagesApi::class.java)
}