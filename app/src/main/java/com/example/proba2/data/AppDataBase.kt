package com.example.proba2.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.proba2.data.base.CatBreedDao
import com.example.proba2.data.model.CatBreedEntity

@Database(
    entities = [
        CatBreedEntity::class,
    ],
    version = 3,
    exportSchema = true,
    autoMigrations = [
//        AutoMigration(from = 1, to = 2),
//        AutoMigration(from = 2, to = 3),
    ],
)
@TypeConverters(
    JsonTypeConvertor::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun catBreedDao(): CatBreedDao

}