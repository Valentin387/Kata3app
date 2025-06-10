package com.kata3.kata3app.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kata3.kata3app.data.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginResult {
    data class Success(val token: String) : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult

    fun loginUser(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.loginUser(username, password)
                if (response.isSuccessful) {
                    _loginResult.value = LoginResult.Success(response.body()?.token ?: "")
                } else {
                    _loginResult.value = LoginResult.Error("Login failed. Invalid credentials.")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Error: ${e.message}")
            }
        }
    }
}