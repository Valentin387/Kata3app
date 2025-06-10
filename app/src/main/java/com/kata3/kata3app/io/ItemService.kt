package com.kata3.kata3app.io

import android.content.Context
import com.kata3.kata3app.BuildConfig
import com.kata3.kata3app.data.repositories.ItemResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ItemService {

    @GET("items")
    suspend fun getItems(
        @Header("Authorization") token: String,
        @Query("type") type: String
    ): Response<List<ItemResponse>>

    companion object Factory {
        private const val BASE_URL = BuildConfig.BASE_URL

        fun create(context: Context): ItemService {
            val client = OkHttpClient.Builder().build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItemService::class.java)
        }
    }
}