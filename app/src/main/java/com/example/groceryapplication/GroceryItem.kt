package com.example.groceryapplication

import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_PENDING

data class GroceryItem(val id: Int = 0,
                       var name: String = "",
                       var listId: Int = -1,
                       var listName: String = "",
                       var status: String = STATUS_PENDING,
                       var amount: Int = 0)
