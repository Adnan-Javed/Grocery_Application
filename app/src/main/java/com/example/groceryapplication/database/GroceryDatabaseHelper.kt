package com.example.groceryapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GroceryDatabase(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "GroceryDatabase.db"
        const val TABLE_GROCERY = "Grocery"
        const val TABLE_GROCERY_ITEM = "GroceryItem"
        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_LIST_ID = "list_id"
        const val KEY_LIST_NAME = "list_name"
        const val KEY_STATUS = "status"
        const val STATUS_PENDING = "Pending"
        const val STATUS_COMPLETED = "Completed"
        const val KEY_AMOUNT = "amount"
        const val KEY_TIMESTAMP = "created_at"

        private const val CREATE_TABLE_GROCERY = "CREATE TABLE IF NOT EXISTS $TABLE_GROCERY (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NAME TEXT, " +
                "$KEY_STATUS TEXT, " +
                "$KEY_TIMESTAMP DATETIME)"

        private const val CREATE_TABLE_GROCERY_ITEM = "CREATE TABLE IF NOT EXISTS $TABLE_GROCERY_ITEM (" +
                "$KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_NAME TEXT, " +
                "$KEY_LIST_ID INTEGER, " +
                "$KEY_LIST_NAME TEXT, " +
                "$KEY_AMOUNT INTEGER, " +
                "$KEY_STATUS TEXT)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_GROCERY)
        db?.execSQL(CREATE_TABLE_GROCERY_ITEM)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.let { database ->
            database.execSQL("DROP TABLE IF EXISTS $TABLE_GROCERY")
            database.execSQL("DROP TABLE IF EXISTS $TABLE_GROCERY_ITEM")
            onCreate(database)
        }
    }
}