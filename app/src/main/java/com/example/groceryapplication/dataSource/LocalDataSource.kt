package com.example.groceryapplication.dataSource

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.groceryapplication.GroceryDatabase
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_AMOUNT
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_ID
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_ID
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_NAME
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_NAME
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_STATUS
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_TIMESTAMP
import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_PENDING
import com.example.groceryapplication.GroceryDatabase.Companion.TABLE_GROCERY
import com.example.groceryapplication.GroceryDatabase.Companion.TABLE_GROCERY_ITEM
import com.example.groceryapplication.model.GroceryItem
import com.example.groceryapplication.model.GroceryList
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val groceryDatabase: GroceryDatabase) {

    private val readableDatabase: SQLiteDatabase by lazy {
        groceryDatabase.readableDatabase
    }
    private val writableDatabase: SQLiteDatabase by lazy {
        groceryDatabase.writableDatabase
    }

    fun createList(groceryList: GroceryList): Long {

        val values = ContentValues().apply {
            put(KEY_NAME, groceryList.name)
            put(KEY_STATUS, groceryList.status)
            put(KEY_TIMESTAMP, getDateTime())
        }

        return writableDatabase.insert(TABLE_GROCERY, null, values)
    }

    fun createItem(listId: Int, listName: String, groceryItem: GroceryItem): Long {

        val values = ContentValues().apply {
            put(KEY_NAME, groceryItem.name)
            put(KEY_LIST_ID, listId)
            put(KEY_LIST_NAME, listName)
            put(KEY_STATUS, groceryItem.status)
            put(KEY_AMOUNT, groceryItem.amount)
        }

        return writableDatabase.insert(TABLE_GROCERY_ITEM, null, values)
    }

    fun updateListStatus(listId: Int, status: String): Int {
        val values = ContentValues().apply {
            put(KEY_STATUS, status)
        }

        return writableDatabase.update(TABLE_GROCERY, values, "$KEY_ID = ?", arrayOf(listId.toString()))
    }

    fun updateItem(groceryItem: GroceryItem): Int {
        val values = ContentValues().apply {
            put(KEY_NAME, groceryItem.name)
            put(KEY_STATUS, groceryItem.status)
            put(KEY_AMOUNT, groceryItem.amount)
        }

        return writableDatabase.update(TABLE_GROCERY_ITEM, values, "$KEY_ID = ?", arrayOf(groceryItem.id.toString()))
    }

    fun deleteItem(id: Int): Int {
        return writableDatabase.delete(TABLE_GROCERY_ITEM,"$KEY_ID = $id", null)
    }

    fun deleteItems(id: Int): Int {
        return writableDatabase.delete(TABLE_GROCERY_ITEM,"$KEY_LIST_ID = $id", null)
    }

    fun deleteList(id: Int): Int {
        return writableDatabase.delete(TABLE_GROCERY,"$KEY_ID = $id", null)
    }

    fun getGroceryList(id: Int): GroceryList {
        val groceryList = GroceryList()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_GROCERY " +
                                                   "WHERE $KEY_ID = $id", null)
        if (cursor.moveToFirst()) {
            groceryList.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            groceryList.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
            groceryList.status = cursor.getString(cursor.getColumnIndex(KEY_STATUS))
            groceryList.timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))
        }
        if (readableDatabase.isOpen) cursor.close()

        return groceryList
    }

    fun getAllLists(): List<GroceryList> {
        val groceryItems = mutableListOf<GroceryList>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_GROCERY " +
                                                   "ORDER BY $KEY_TIMESTAMP DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val groceryList = GroceryList(
                    id = cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    name = cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    status = cursor.getString(cursor.getColumnIndex(KEY_STATUS)),
                    timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))
                )
                groceryItems.add(groceryList)
            } while (cursor.moveToNext())
        }

        if (readableDatabase.isOpen) cursor.close()

        return groceryItems
    }

    fun getLists(status: String): List<GroceryList> {
        val groceryItems = mutableListOf<GroceryList>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_GROCERY " +
                                                   "WHERE $KEY_STATUS = '$status' " +
                                                   "ORDER BY $KEY_TIMESTAMP DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val groceryList = GroceryList(
                    id = cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    name = cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    status = cursor.getString(cursor.getColumnIndex(KEY_STATUS)),
                    timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))
                )
                groceryItems.add(groceryList)
            } while (cursor.moveToNext())
        }

        if (readableDatabase.isOpen) cursor.close()

        return groceryItems
    }

    fun getItems(status: String): List<GroceryItem> {
        val groceryItems = mutableListOf<GroceryItem>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_GROCERY_ITEM " +
                                                   "WHERE $KEY_STATUS = '$status' " +
                                                   "ORDER BY $KEY_NAME ASC", null)

        if (cursor.moveToFirst()) {
            do {
                val groceryItem = GroceryItem(
                    id = cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    name = cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    status = cursor.getString(cursor.getColumnIndex(KEY_STATUS)),
                    amount = cursor.getInt(cursor.getColumnIndex(KEY_AMOUNT))
                )
                groceryItems.add(groceryItem)
            } while (cursor.moveToNext())
        }

        if (readableDatabase.isOpen) cursor.close()

        return groceryItems
    }

    fun getRecentListItems(): List<GroceryItem> {
        val groceryList = GroceryList()
        val groceryItems = mutableListOf<GroceryItem>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_GROCERY " +
                                                   "WHERE $KEY_STATUS = '$STATUS_PENDING' " +
                                                   "ORDER BY $KEY_TIMESTAMP DESC LIMIT 1", null)
        if (cursor.moveToFirst()) {

            if (cursor.moveToFirst()) {
                groceryList.id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                groceryList.name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                groceryList.status = cursor.getString(cursor.getColumnIndex(KEY_STATUS))
                groceryList.timestamp = cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMP))
            }
            if (readableDatabase.isOpen) cursor.close()

            val itemsCursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_GROCERY_ITEM " +
                                                            "WHERE $KEY_LIST_ID = ${groceryList.id} " +
                                                            "ORDER BY $KEY_NAME ASC", null)
            if (itemsCursor.moveToFirst()) {
                do {
                    val groceryItem = GroceryItem(
                        id = itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_ID)),
                        name = itemsCursor.getString(itemsCursor.getColumnIndex(KEY_NAME)),
                        listId = itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_LIST_ID)),
                        listName = itemsCursor.getString(itemsCursor.getColumnIndex(KEY_LIST_NAME)),
                        status = itemsCursor.getString(itemsCursor.getColumnIndex(KEY_STATUS)),
                        amount = itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_AMOUNT))
                    )
                    groceryItems.add(groceryItem)
                } while (itemsCursor.moveToNext())
            }

            if (readableDatabase.isOpen) itemsCursor.close()
        }

        return groceryItems
    }

    fun getListItems(listId: Int): List<GroceryItem> {
        val groceryItems = mutableListOf<GroceryItem>()
        val itemsCursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_GROCERY_ITEM " +
                                                        "WHERE $KEY_LIST_ID = $listId " +
                                                        "ORDER BY $KEY_NAME ASC", null)
        if (itemsCursor.moveToFirst()) {
            do {
                val groceryItem = GroceryItem(
                    id = itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_ID)),
                    name = itemsCursor.getString(itemsCursor.getColumnIndex(KEY_NAME)),
                    listId = itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_LIST_ID)),
                    listName = itemsCursor.getString(itemsCursor.getColumnIndex(KEY_LIST_NAME)),
                    status = itemsCursor.getString(itemsCursor.getColumnIndex(KEY_STATUS)),
                    amount = itemsCursor.getInt(itemsCursor.getColumnIndex(KEY_AMOUNT))
                )
                groceryItems.add(groceryItem)
            } while (itemsCursor.moveToNext())
        }
        if (readableDatabase.isOpen) itemsCursor.close()

        return groceryItems
    }

    fun duplicateList(listId: Int) {
        val groceryList = GroceryList(name = "Duplicate")
        val newId = createList(groceryList)

        val groceryItems = getListItems(listId).map {
            it.listName = groceryList.name
            it.status = STATUS_PENDING
            it.listId = newId.toInt()
            it
        }

        for (groceryItem in groceryItems) {
            createItem(newId.toInt(), groceryList.name, groceryItem)
        }
    }

    private fun getDateTime(): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

}