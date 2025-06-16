package com.example.proba2.data.base

import android.content.Context
import com.example.proba2.data.AppDatabase
import com.example.proba2.data.AppDatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(builder: AppDatabaseBuilder): AppDatabase {
        return builder.build()
    }

    @Provides
    fun provideCatBreedDao(database: AppDatabase): CatBreedDao {
        return database.catBreedDao()
    }

    @Singleton
    @Provides
    fun provideAppDatabaseBuilder(@ApplicationContext context: Context): AppDatabaseBuilder {
        return AppDatabaseBuilder(context)
    }

}

