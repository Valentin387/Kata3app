package com.kata3.kata3app.ui.main.details


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.DTO.ItemUpdateRequest
import com.kata3.kata3app.data.repositories.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class DetailsResult {
    data class Success(val message: String, val isDeleted: Boolean = false) : DetailsResult()
    data class Error(val message: String) : DetailsResult()
}

class DetailsViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _item = MutableLiveData<ItemResponse>()
    val item: LiveData<ItemResponse> = _item

    private val _result = MutableLiveData<DetailsResult>()
    val result: LiveData<DetailsResult> = _result

    fun fetchItem(token: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = itemRepository.fetchItem(token, id)
                if (response != null) {
                    _item.postValue(response)
                    _result.postValue(null)
                } else {
                    _result.postValue(DetailsResult.Error("Failed to load item."))
                }
            } catch (e: Exception) {
                _result.postValue(DetailsResult.Error("Error: ${e.message()}"))
            }
        }
    }

    fun updateItem(token: String, id: String, request: ItemUpdateRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = itemRepository.updateItem(token, id, request)
                if (response != null) {
                    _item.postValue(response)
                    _result.postValue(DetailsResult.Success("Item updated successfully!"))
                } else {
                    _result.postValue(DetailsResult.Error("Failed to update item."))
                }
            } catch (e: Exception) {
                _result.postValue(DetailsResult.Error("Error: ${e.message()}"))
            }
        }
    }

    fun deleteItem(token: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = itemRepository.deleteItem(token, id)
                if (success) {
                    _result.postValue(DetailsResult.Success("Item deleted successfully!", true))
                } else {
                    _result.postValue(DetailsResult.Error("Failed to delete item."))
                }
            } catch (e: Exception) {
                _result.postValue(DetailsResult.Error("Error: ${e.message()}"))
            }
        }
    }
}