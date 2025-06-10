package com.kata3.kata3app.data.repositories

import android.util.Log
import com.kata3.kata3app.data.DTO.SignInRequest
import com.kata3.kata3app.data.DTO.SignUpRequest
import com.kata3.kata3app.io.AuthService
import retrofit2.Response

data class SignUpResponse(val token: String)

class AuthRepository(private val authService: AuthService) {

    suspend fun registerUser(username: String, password: String): Response<SignUpResponse> {
        return try {
            val response = authService.postRegister(SignUpRequest(username, password))
            if (response.isSuccessful) {
                Log.d("AuthRepository", "Registration successful")
            } else {
                Log.e("AuthRepository", "Registration failed. Code: ${response.code()}")
            }
            response
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error registering user", e)
            throw e
        }
    }

    suspend fun loginUser(username: String, password: String): Response<SignUpResponse> {
        return try {
            val response = authService.postLogin(SignInRequest(username))
            if (response.isSuccessful) {
                Log.d("AuthRepository", "Login successful")
            } else {
                Log.e("AuthRepository", "Login failed. Code: ${response.code()}")
            }
            response
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error logging in user", e)
            throw e
        }
    }
}