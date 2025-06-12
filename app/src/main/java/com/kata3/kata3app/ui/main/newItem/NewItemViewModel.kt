package com.kata3.kata3app.ui.main.newItem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.DTO.ItemCreateRequest
import com.kata3.kata3app.data.repositories.ItemRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class CreateItemResult {
    data class Success(val item: ItemResponse) : CreateItemResult()
    data class Error(val message: String) : CreateItemResult()
}

class NewItemViewModel(
    private val itemRepository: ItemRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _createResult = MutableLiveData<CreateItemResult>()
    val createResult: LiveData<CreateItemResult> = _createResult

    fun createItem(token: String, request: ItemCreateRequest) {
        println("Creating item with token: $token")
        viewModelScope.launch(dispatcher) {
            try {
                val item = itemRepository.createItem(token, request)
                if (item != null) {
                    _createResult.postValue(CreateItemResult.Success(item))
                } else {
                    _createResult.postValue(CreateItemResult.Error("Failed to create item."))
                }
            } catch (e: Exception) {
                _createResult.postValue(CreateItemResult.Error("Error: ${e.message}"))
            }
        }
    }
}