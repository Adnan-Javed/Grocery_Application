package com.example.groceryapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.groceryapplication.BaseApplication
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_ID
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_NAME
import com.example.groceryapplication.model.GroceryItem
import com.example.groceryapplication.databinding.ActivityCreateItemBinding
import com.example.groceryapplication.di.ViewModelModules.ViewModelFactory
import com.example.groceryapplication.viewModel.GroceryViewModel
import javax.inject.Inject

class CreateItemActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory : ViewModelFactory
    private lateinit var binding: ActivityCreateItemBinding
    lateinit var groceryViewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityCreateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listId = intent.getIntExtra(KEY_LIST_ID, -1)
        val listName = intent.getStringExtra(KEY_LIST_NAME).toString()

        groceryViewModel = ViewModelProvider(this, viewModelFactory).get(GroceryViewModel::class.java)
        binding.actionCreate.setOnClickListener {
            if (binding.inputName.text.isNullOrEmpty().not() &&
                binding.inputAmount.text.isNullOrEmpty().not()) {

                    val groceryItem = GroceryItem(
                        name = binding.inputName.text.toString(),
                        amount =  binding.inputAmount.text.toString().toInt())
                groceryViewModel.createItem(listId, listName, groceryItem)
                finish()
            }
        }
    }
}