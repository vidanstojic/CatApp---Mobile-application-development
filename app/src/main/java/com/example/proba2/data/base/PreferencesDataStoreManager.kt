package com.example.proba2.data.base

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferencesDataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "settings")
        val IS_DB_INITIALIZED = booleanPreferencesKey("is_db_initialized")
        val LAST_SYNC_TIMESTAMP = longPreferencesKey("last_sync_timestamp")
    }

    val isDbInitializedFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[IS_DB_INITIALIZED] ?: false }

    val lastSyncTimestampFlow: Flow<Long> = context.dataStore.data
        .map { prefs -> prefs[LAST_SYNC_TIMESTAMP] ?: 0L }

    suspend fun setDbInitialized(value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_DB_INITIALIZED] = value
        }
    }

    suspend fun setLastSyncTimestamp(timestamp: Long) {
        context.dataStore.edit { prefs ->
            prefs[LAST_SYNC_TIMESTAMP] = timestamp
        }
    }
}