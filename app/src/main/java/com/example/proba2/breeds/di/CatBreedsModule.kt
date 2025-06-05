package com.example.proba2.breeds.di

import com.example.proba2.breeds.api.CatBreedApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CatBreedsModule {

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit) = retrofit.create<CatBreedApi>()
}