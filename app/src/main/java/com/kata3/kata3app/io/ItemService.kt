package com.kata3.kata3app.io

import android.content.Context
import com.kata3.kata3app.BuildConfig
import com.kata3.kata3app.data.DTO.ItemCreateRequest
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.DTO.ItemUpdateRequest
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ItemService {

    @GET("items")
    suspend fun getItems(
        @Header("Authorization") token: String,
        @Query("type") type: String
    ): Response<List<ItemResponse>>

    @GET("items/{id}")
    suspend fun getItem(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ItemResponse>

    @POST("items")
    suspend fun createItem(
        @Header("Authorization") token: String,
        @Body request: ItemCreateRequest
    ): Response<ItemResponse>

    @PATCH("items/{id}")
    suspend fun updateItem(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: ItemUpdateRequest
    ): Response<ItemResponse>

    @DELETE("items/{id}")
    suspend fun deleteItem(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

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