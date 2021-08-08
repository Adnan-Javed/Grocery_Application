package com.example.groceryapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroceryItemsAdapter(private val groceryItems: MutableList<GroceryItem> = mutableListOf()): RecyclerView.Adapter<GroceryItemsAdapter.GroceryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery_done, parent, false)
        return GroceryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroceryItemViewHolder, position: Int) {
        holder.onBindItem(groceryItems[position])
    }

    override fun getItemCount(): Int {
        return groceryItems.size
    }

    fun updateItems(items: List<GroceryItem>) {
        groceryItems.clear()
        groceryItems.addAll(items)
        notifyDataSetChanged()
    }

    inner class GroceryItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.item_name)
        private val amount: TextView = itemView.findViewById(R.id.item_amount)

        fun onBindItem(groceryItem: GroceryItem) {
            name.text = groceryItem.name
            amount.text = "RS. ${groceryItem.amount}"
        }
    }
}