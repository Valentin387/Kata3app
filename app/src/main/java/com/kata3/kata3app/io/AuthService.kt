package com.kata3.kata3app.io

import android.content.Context
import com.kata3.kata3app.BuildConfig
import com.kata3.kata3app.data.DTO.SignInRequest
import com.kata3.kata3app.data.DTO.SignUpRequest
import com.kata3.kata3app.data.repositories.SignUpResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/register")
    suspend fun postRegister(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("auth/login")
    suspend fun postLogin(@Body request: SignInRequest): Response<SignUpResponse>

    companion object Factory {
        private const val BASE_URL = BuildConfig.BASE_URL

        fun create(context: Context): AuthService {
            val client = OkHttpClient.Builder().build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AuthService::class.java)
        }
    }
}