package com.example.groceryapplication.model

import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_PENDING

data class GroceryList(var id: Int = 0,
                       var name: String = "",
                       var status: String = STATUS_PENDING,
                       var timestamp: String = "")
