package com.kata3.kata3app.data.repositories

import android.util.Log
import com.kata3.kata3app.data.DTO.ItemCreateRequest
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.DTO.ItemUpdateRequest
import com.kata3.kata3app.io.ItemService

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

    suspend fun fetchItem(token: String, id: String): ItemResponse? {
        return try {
            val response = itemService.getItem("Bearer $token", id)
            if (response.isSuccessful) {
                Log.d("ItemRepository", "Fetched item: ${response.body()}")
                response.body()
            } else {
                Log.e("ItemRepository", "Failed to fetch item. Code: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error fetching item", e)
            null
        }
    }

    suspend fun createItem(token: String, request: ItemCreateRequest): ItemResponse? {
        return try {
            val response = itemService.createItem("Bearer $token", request)
            if (response.isSuccessful) {
                Log.d("ItemRepository", "Created item: ${response.body()}")
                response.body()
            } else {
                Log.e("ItemRepository", "Failed to create item. Code: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error creating item", e)
            null
        }
    }

    suspend fun updateItem(token: String, id: String, request: ItemUpdateRequest): ItemResponse? {
        return try {
            val response = itemService.updateItem("Bearer $token", id, request)
            if (response.isSuccessful) {
                Log.d("ItemRepository", "Updated item: ${response.body()}")
                response.body()
            } else {
                Log.e("ItemRepository", "Failed to update item. Code: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error updating item", e)
            null
        }
    }

    suspend fun deleteItem(token: String, id: String): Boolean {
        return try {
            val response = itemService.deleteItem("Bearer $token", id)
            if (response.isSuccessful) {
                Log.d("ItemRepository", "Deleted item: $id")
                true
            } else {
                Log.e("ItemRepository", "Failed to delete item. Code: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e("ItemRepository", "Error deleting item", e)
            false
        }
    }
}