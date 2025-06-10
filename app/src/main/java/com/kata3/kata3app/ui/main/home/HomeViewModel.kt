package com.kata3.kata3app.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.repositories.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _itemList = MutableLiveData<List<ItemResponse>>()
    val itemList: LiveData<List<ItemResponse>> = _itemList

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchItems(token: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val items = itemRepository.fetchItems(token, type)
                if (items != null) {
                    _itemList.postValue(items)
                    _errorMessage.postValue(null)
                } else {
                    _errorMessage.postValue("Failed to load items. Please try again.")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}