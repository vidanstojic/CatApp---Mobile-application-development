package com.example.proba2.data

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDatabaseBuilder @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun build(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rma.db"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
}
