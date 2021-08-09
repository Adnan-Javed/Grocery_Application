package com.example.groceryapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_COMPLETED
import com.example.groceryapplication.GroceryDatabase.Companion.STATUS_PENDING
import com.example.groceryapplication.model.GroceryItem
import com.example.groceryapplication.R
import com.google.android.material.checkbox.MaterialCheckBox

class GroceryHomeAdapter(private val groceryItems: MutableList<GroceryItem> = mutableListOf(),
                         private var itemClickListener: GroceryItemClickListener? = null): RecyclerView.Adapter<GroceryHomeAdapter.GroceryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_grocery, parent, false)
        return GroceryItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: GroceryItemViewHolder, position: Int) {
        holder.onBindItem(groceryItems[position])
    }

    override fun getItemCount(): Int {
        return groceryItems.size
    }

    fun getItem(position: Int): GroceryItem {
        return groceryItems[position]
    }

    fun updateItems(items: List<GroceryItem>) {
        groceryItems.clear()
        groceryItems.addAll(items)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        groceryItems.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setGroceryItemClickListener(listener: GroceryItemClickListener) {
        this.itemClickListener = listener
    }

    interface GroceryItemClickListener {
        fun onGroceryItemChecked(groceryItem: GroceryItem)
    }

    inner class GroceryItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.item_name)
        private val amount: TextView = itemView.findViewById(R.id.item_amount)
        private val actionCompleted: MaterialCheckBox = itemView.findViewById(R.id.action_done)

        fun onBindItem(groceryItem: GroceryItem) {

            name.text = groceryItem.name
            amount.text = "RS. ${groceryItem.amount}"
            actionCompleted.isChecked = when (groceryItem.status) {
                STATUS_PENDING -> false
                else -> true
            }
            actionCompleted.tag = groceryItem
            actionCompleted.setOnCheckedChangeListener { buttonView, isChecked ->
                val item = buttonView.tag as GroceryItem
                when (isChecked) {
                    true -> item.status = STATUS_COMPLETED
                    false -> item.status = STATUS_PENDING
                }
                itemClickListener?.onGroceryItemChecked(item)
            }
        }
    }
}