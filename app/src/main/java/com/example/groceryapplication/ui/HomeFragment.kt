package com.example.groceryapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapplication.BaseApplication
import com.example.groceryapplication.model.GroceryItem
import com.example.groceryapplication.databinding.FragmentHomeBinding
import com.example.groceryapplication.di.ViewModelModules.ViewModelFactory
import com.example.groceryapplication.viewModel.GroceryViewModel
import javax.inject.Inject

class HomeFragment : Fragment(), GroceryHomeAdapter.GroceryItemClickListener {

    @Inject lateinit var viewModelFactory : ViewModelFactory
    lateinit var groceryViewModel: GroceryViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val groceryHomeAdapter: GroceryHomeAdapter by lazy {
        GroceryHomeAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        groceryViewModel = ViewModelProvider(this, viewModelFactory).get(GroceryViewModel::class.java)
        binding.recyclerViewHome.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerViewHome.adapter = groceryHomeAdapter
        groceryHomeAdapter.setGroceryItemClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        groceryViewModel.groceryRecentItemsLive.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) groceryHomeAdapter.updateItems(it)
        })
    }

    override fun onResume() {
        super.onResume()
        groceryViewModel.getMostRecentItems()
    }

    override fun onGroceryItemChecked(groceryItem: GroceryItem) {
        groceryViewModel.updateItem(groceryItem)
    }
}