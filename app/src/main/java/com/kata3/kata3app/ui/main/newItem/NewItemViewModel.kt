package com.kata3.kata3app.ui.main.newItem


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.DTO.ItemCreateRequest
import com.kata3.kata3app.data.repositories.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class CreateItemResult {
    data class Success(val item: ItemResponse) : CreateResult()
    data class Error(val message: String) : CreateResult()
}

class NewItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _createResult = MutableLiveData<CreateResult>()
    val createResult: LiveData<CreateResult> = _createResult

    fun createItem(token: String, request: ItemCreateRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val item = itemRepository.createItem(token, request)
                if (item != null) {
                    _createResult.postValue(CreateResult.Success(item)))
                } else {
                    _createResult.postValue(CreateResult.Error("Failed to create item."))
                }
            } catch (e: Exception) {
                _createResult.postValue(CreateResult.Error("Error: ${e.message}"))
            }
        }
    }
}