package com.example.proba2.data.base

import androidx.room.*
import com.example.proba2.data.model.CatBreedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatBreedDao {
    @Query("SELECT * FROM cat_breeds")
    fun getAll(): Flow<List<CatBreedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(breeds: List<CatBreedEntity>)

    @Query("DELETE FROM cat_breeds")
    suspend fun clearAll()

    @Query("SELECT * FROM cat_breeds WHERE id = :breedId LIMIT 1")
    suspend fun getById(breedId: String): CatBreedEntity


    @Query("SELECT * FROM cat_breeds WHERE name LIKE :query")
    suspend fun search(query: String): List<CatBreedEntity>
}
