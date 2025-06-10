package com.kata3.kata3app.data.repositories

import android.util.Log
import com.kata3.kata3app.io.ItemService

data class ItemResponse(
    val id: String,
    val name: String,
    val description: String,
    val type: String
)

class ItemRepository(private val itemService: ItemService) {

    suspend fun fetchItems(token: String, type: String): List<ItemResponse>? {
        return try {
            val response = itemService.getItems("Bearer $token", type)
            if (response.isSuccessful) {
                Log.d("ItemRepository", "Fetched items: ${response.body()}")
                response.body()
            } else {
                Log.e("ItemRepository", "Failed to fetch items. Code: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error fetching items", e)
            null
        }
    }
}