package com.example.groceryapplication

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GroceryRepository @Inject constructor(private val localDataSource: LocalDataSource) {

    suspend fun createList(groceryList: GroceryList): Long = withContext(Dispatchers.IO) {
        localDataSource.createList(groceryList)
    }

    suspend fun getList(id: Int): GroceryList = withContext(Dispatchers.IO) {
        localDataSource.getGroceryList(id)
    }

    suspend fun getLists(): List<GroceryList> = withContext(Dispatchers.IO) {
        localDataSource.getAllLists()
    }

    suspend fun getListItems(listId: Int): List<GroceryItem> = withContext(Dispatchers.IO) {
        localDataSource.getListItems(listId)
    }

    fun duplicateList(listId: Int) = CoroutineScope(Dispatchers.IO).launch {
        localDataSource.duplicateList(listId)
    }

    suspend fun deleteList(listId: Int) = withContext(Dispatchers.IO) {
        localDataSource.deleteList(listId)
    }

    suspend fun createItem(listId: Int, listName: String,groceryItem: GroceryItem): Long = withContext(Dispatchers.IO) {
        localDataSource.createItem(listId, listName, groceryItem)
    }

    suspend fun deleteItem(id: Int) = withContext(Dispatchers.IO) {
        localDataSource.deleteItem(id)
    }

    suspend fun deleteItems(listId: Int) = withContext(Dispatchers.IO) {
        localDataSource.deleteItems(listId)
    }

    suspend fun updateItem(groceryItem: GroceryItem) = withContext(Dispatchers.IO) {
        localDataSource.updateItem(groceryItem)
    }

    suspend fun updateListStatus(listId: Int, status: String) = withContext(Dispatchers.IO) {
        localDataSource.updateListStatus(listId, status)
    }

    suspend fun fetchRecentItems(): List<GroceryItem> = withContext(Dispatchers.IO) {
        localDataSource.getRecentListItems()
    }
}