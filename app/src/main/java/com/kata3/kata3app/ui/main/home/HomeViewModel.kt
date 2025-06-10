package com.kata3.kata3app.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kata3.kata3app.data.repositories.ItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

data class ItemResponse(val id: String, val name: String, val type: String)

class HomeViewModel(private val itemRepository: ItemRepository) : ViewModel() {

    private val _itemList = MutableLiveData<List<ItemResponse>>()
    val itemList: LiveData<List<ItemResponse>> = _itemList

    fun fetchItems(token: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val items = itemRepository.fetchItems(token, type) ?: emptyList()
            _itemList.postValue(items)
        }
    }
}