package com.example.proba2.user.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proba2.user.model.UserProfile
import com.example.proba2.user.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val repository: UserProfileRepository
) : ViewModel() {

    private val _profileCreated = MutableStateFlow(false)
    val profileCreated: StateFlow<Boolean> = _profileCreated.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    val userProfile: Flow<UserProfile?> = repository.userProfile

    fun createProfile(fullName: String, nickname: String, email: String) {
        if (!isValid(fullName, nickname, email)) return

        val profile = UserProfile(fullName, nickname, email)

        viewModelScope.launch {
            repository.saveUserProfile(profile)
            _profileCreated.value = true
        }
    }

    private fun isValid(fullName: String, nickname: String, email: String): Boolean {
        return when {
            fullName.isBlank() -> {
                _errorMessage.value = "Ime i prezime su obavezni"
                false
            }
            !nickname.matches(Regex("^[a-zA-Z0-9_]+$")) -> {
                _errorMessage.value = "Nickname može sadržati samo slova, brojeve i underscore"
                false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _errorMessage.value = "Nevalidna email adresa"
                false
            }
            else -> {
                _errorMessage.value = null
                true
            }
        }
    }

}
