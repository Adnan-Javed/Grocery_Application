package com.example.groceryapplication

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_ID
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_NAME
import com.example.groceryapplication.databinding.FragmentGroceryItemsBinding
import com.example.groceryapplication.databinding.FragmentGroceryListBinding
import com.example.groceryapplication.di.ViewModelModules.ViewModelFactory
import javax.inject.Inject

class GroceryItemsFragment : Fragment() {

    @Inject lateinit var viewModelFactory : ViewModelFactory
    private lateinit var groceryViewModel: GroceryViewModel
    private var _binding: FragmentGroceryItemsBinding? = null
    private val binding get() = _binding!!
    private val groceryItemsAdapter: GroceryItemsAdapter by lazy {
        GroceryItemsAdapter()
    }
    private var listId: Int = -1
    private var listName: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        listId = arguments?.getInt(KEY_LIST_ID) ?: -1
        listName = arguments?.getString(KEY_LIST_NAME).toString()

        (requireActivity() as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as MainActivity).supportActionBar?.title = listName
        setHasOptionsMenu(true)

        _binding = FragmentGroceryItemsBinding.inflate(inflater, container, false)
        groceryViewModel = ViewModelProvider(this, viewModelFactory).get(GroceryViewModel::class.java)
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerViewItems.adapter = groceryItemsAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        groceryViewModel.groceryItemsLive.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) groceryItemsAdapter.updateItems(it)
        })
    }

    override fun onResume() {
        super.onResume()
        groceryViewModel.getItems(listId)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_grocery_items, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                launchActivity()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun launchActivity() {
        val intent = Intent(requireActivity(), CreateItemActivity::class.java)
        intent.putExtra(KEY_LIST_ID, listId)
        intent.putExtra(KEY_LIST_NAME, listName)
        startActivity(intent)
    }
}