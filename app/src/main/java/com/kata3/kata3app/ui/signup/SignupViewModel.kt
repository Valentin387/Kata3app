package com.kata3.kata3app.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kata3.kata3app.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers

sealed class RegisterResult {
    data class Success(val token: String) : RegisterResult()
    data class Error(val message: String) : RegisterResult()
}

class SignupViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registerResult = MutableStateFlow<RegisterResult?>(null)
    val registerResult: StateFlow<RegisterResult?> = _registerResult

    fun registerUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.registerUser(username, password)
                if (response.isSuccessful) {
                    _registerResult.value = RegisterResult.Success(response.body()?.token ?: "")
                } else {
                    _registerResult.value = RegisterResult.Error("Registration failed. Username may already exist.")
                }
            } catch (e: Exception) {
                _registerResult.value = RegisterResult.Error("Error: ${e.message}")
            }
        }
    }
}