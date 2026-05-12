package com.example.proba2.data

import android.content.Context
import com.example.proba2.data.base.PreferencesDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun providePreferencesDataStoreManager(@ApplicationContext context: Context): PreferencesDataStoreManager {
        return PreferencesDataStoreManager(context)
    }
}
