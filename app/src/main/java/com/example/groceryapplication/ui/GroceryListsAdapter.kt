package com.example.groceryapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_PENDING
import com.example.groceryapplication.model.GroceryList
import com.example.groceryapplication.R
import com.google.android.material.checkbox.MaterialCheckBox

class GroceryListsAdapter(private val groceryItems: MutableList<GroceryList> = mutableListOf(),
                          private var itemClickListener: GroceryListClickListener? = null): RecyclerView.Adapter<GroceryListsAdapter.GroceryListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery_list, parent, false)
        return GroceryListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroceryListViewHolder, position: Int) {
        holder.onBindItem(groceryItems[position])
    }

    override fun getItemCount(): Int {
        return groceryItems.size
    }

    fun updateItems(items: List<GroceryList>) {
        groceryItems.clear()
        groceryItems.addAll(items)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): GroceryList {
        return groceryItems[position]
    }

    fun removeItem(position: Int) {
        groceryItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setGroceryListClickListener(listener: GroceryListClickListener) {
        this.itemClickListener = listener
    }

    interface GroceryListClickListener {
        fun onGroceryListClicked(groceryList: GroceryList)
    }

    inner class GroceryListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.item_name)
        private val timestamp: TextView = itemView.findViewById(R.id.item_created_at)
        private val status: TextView = itemView.findViewById(R.id.item_status)
        private val actionCompleted: MaterialCheckBox = itemView.findViewById(R.id.action_done)
        val viewBackground: View = itemView.findViewById(R.id.view_background)
        val viewForeground: View = itemView.findViewById(R.id.view_foreground)

        fun onBindItem(groceryList: GroceryList) {

            name.text = groceryList.name
            timestamp.text = groceryList.timestamp
            status.text = groceryList.status
            actionCompleted.isChecked = when (groceryList.status) {
                STATUS_PENDING -> false
                else -> true
            }
            actionCompleted.isClickable = false
            actionCompleted.alpha = 0.5f
            itemView.tag = groceryList
            itemView.setOnClickListener {
                itemClickListener?.onGroceryListClicked(it.tag as GroceryList)
            }
        }
    }
}