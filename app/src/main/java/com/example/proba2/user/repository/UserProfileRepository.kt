package com.example.proba2.user.repository

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.proba2.user.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_profile")

class UserProfileRepository(private val context: Context) {

    private object Keys {
        val FULL_NAME = stringPreferencesKey("full_name")
        val NICKNAME = stringPreferencesKey("nickname")
        val EMAIL = stringPreferencesKey("email")
    }

    val userProfile: Flow<UserProfile?> = context.dataStore.data.map { prefs ->
        val name = prefs[Keys.FULL_NAME]
        val nickname = prefs[Keys.NICKNAME]
        val email = prefs[Keys.EMAIL]
        if (name != null && nickname != null && email != null) {
            UserProfile(name, nickname, email)
        } else null
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.FULL_NAME] = profile.fullName
            prefs[Keys.NICKNAME] = profile.nickname
            prefs[Keys.EMAIL] = profile.email
        }
    }
}
