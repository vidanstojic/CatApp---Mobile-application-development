package com.example.proba2.networking.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import com.example.proba2.networking.serialization.NetworkingJson
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.MediaType.Companion.toMediaType

@Module
@InstallIn(SingletonComponent::class)
object NetworkingModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        /*
         * Order of okhttp interceptors is important.
         * If logging was first it would not log the custom header.
         */
        .addInterceptor {
            val updatedRequest = it.request().newBuilder()
//                .url("https://servis.raf.edu.rs/users")
                .addHeader("x-api-key", "live_w8GyQ3lo3gZySK5BVFAIAgDUPt7vpkxltJ3tmLTvgT9yy2L6OB8mzmysp2HkiFjI")
                .build()

            it.proceed(updatedRequest)
        }
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        )
        .build()

    @Singleton
    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(" https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(NetworkingJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

}