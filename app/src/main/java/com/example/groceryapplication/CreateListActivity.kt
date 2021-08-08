package com.example.groceryapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.groceryapplication.databinding.ActivityCreateListBinding
import com.example.groceryapplication.di.ViewModelModules.ViewModelFactory
import javax.inject.Inject

class CreateListActivity : AppCompatActivity() {

    @Inject lateinit var viewModelFactory : ViewModelFactory
    private lateinit var binding: ActivityCreateListBinding
    lateinit var groceryViewModel: GroceryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityCreateListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        groceryViewModel = ViewModelProvider(this, viewModelFactory).get(GroceryViewModel::class.java)
        binding.actionCreate.setOnClickListener {
            if (binding.inputName.text.isNullOrEmpty().not()) {

                val groceryList = GroceryList(name = binding.inputName.text.toString())
                groceryViewModel.createList(groceryList)
                finish()
            }
        }
    }
}