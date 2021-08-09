package com.example.groceryapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_COMPLETED
import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_PENDING
import com.example.groceryapplication.model.GroceryItem
import com.example.groceryapplication.model.GroceryList
import com.example.groceryapplication.repository.GroceryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class GroceryViewModel @Inject constructor(private val groceryRepository: GroceryRepository): ViewModel() {

    val groceryItemsLive = MutableLiveData<List<GroceryItem>>()
    val groceryRecentItemsLive = MutableLiveData<List<GroceryItem>>()
    val groceryListLive = MutableLiveData<List<GroceryList>>()

    fun createList(groceryList: GroceryList) = viewModelScope.launch {
        groceryRepository.createList(groceryList)
    }

    fun getAllLists() = viewModelScope.launch {
        groceryListLive.value = groceryRepository.getLists()
    }

    fun duplicateList(listId: Int) = viewModelScope.launch {
        groceryRepository.duplicateList(listId)
        getAllLists()
    }

    fun getItems(listId: Int) = viewModelScope.launch {
        groceryItemsLive.value = groceryRepository.getListItems(listId)
    }

    fun getMostRecentItems() = viewModelScope.launch {
        groceryRecentItemsLive.value = groceryRepository.fetchRecentItems()
    }

    fun createItem(listId: Int, listName: String, groceryItem: GroceryItem) = viewModelScope.launch {
        groceryRepository.createItem(listId, listName, groceryItem)
    }

    fun updateItem(groceryItem: GroceryItem) = viewModelScope.launch {
        val rowsAffected = groceryRepository.updateItem(groceryItem)
        if (rowsAffected > 0) {
            val groceryItems = groceryRepository.getListItems(groceryItem.listId)
            when (groceryItems.find { it.status == STATUS_PENDING }) {
                null -> groceryRepository.updateListStatus(groceryItem.listId, STATUS_COMPLETED)
                else -> groceryRepository.updateListStatus(groceryItem.listId, STATUS_PENDING)
            }
        }
    }

    fun deleteListAndItems(listId: Int) = viewModelScope.launch {
        groceryRepository.deleteItems(listId)
        groceryRepository.deleteList(listId)
    }
}