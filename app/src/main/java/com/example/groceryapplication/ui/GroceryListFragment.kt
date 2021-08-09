package com.example.groceryapplication.ui

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.groceryapplication.BaseApplication
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_ID
import com.example.groceryapplication.GroceryDatabase.Companion.KEY_LIST_NAME
import com.example.groceryapplication.model.GroceryList
import com.example.groceryapplication.R
import com.example.groceryapplication.databinding.FragmentGroceryListBinding
import com.example.groceryapplication.di.ViewModelModules.ViewModelFactory
import com.example.groceryapplication.viewModel.GroceryViewModel
import javax.inject.Inject

class GroceryListFragment : Fragment(), GroceryListsAdapter.GroceryListClickListener {

    @Inject lateinit var viewModelFactory : ViewModelFactory
    private lateinit var groceryViewModel: GroceryViewModel
    private var _binding: FragmentGroceryListBinding? = null
    private val binding get() = _binding!!
    private val groceryListsAdapter: GroceryListsAdapter by lazy {
        GroceryListsAdapter()
    }
    private val navController: NavController by lazy {
        findNavController()
    }
    private var lists = mutableListOf<GroceryList>()
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        override fun onMove(recyclerView: RecyclerView,
                            viewHolder: RecyclerView.ViewHolder,
                            target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            viewHolder?.let {
                val foregroundView = (viewHolder as GroceryListsAdapter.GroceryListViewHolder).viewForeground
                getDefaultUIUtil().onSelected(foregroundView)
            }
        }

        override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?,
                                     dX: Float,
                                     dY: Float,
                                     actionState: Int,
                                     isCurrentlyActive: Boolean) {
            val foregroundView = (viewHolder as GroceryListsAdapter.GroceryListViewHolder).viewForeground
            getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            val foregroundView = (viewHolder as GroceryListsAdapter.GroceryListViewHolder).viewForeground
            getDefaultUIUtil().clearView(foregroundView)
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                 dX: Float,
                                 dY: Float,
                                 actionState: Int,
                                 isCurrentlyActive: Boolean) {
            val foregroundView = (viewHolder as GroceryListsAdapter.GroceryListViewHolder).viewForeground
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val item = groceryListsAdapter.getItem(viewHolder.absoluteAdapterPosition)
            groceryListsAdapter.removeItem(viewHolder.absoluteAdapterPosition)
            groceryViewModel.deleteListAndItems(item.id)
        }

        override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
            return super.convertToAbsoluteDirection(flags, layoutDirection)
        }
    }

    private val itemTouchHelper: ItemTouchHelper by lazy {
        ItemTouchHelper(itemTouchHelperCallback)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentGroceryListBinding.inflate(inflater, container, false)
        groceryViewModel = ViewModelProvider(this, viewModelFactory).get(GroceryViewModel::class.java)
        binding.recyclerViewGroceryList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerViewGroceryList.adapter = groceryListsAdapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewGroceryList)
        groceryListsAdapter.setGroceryListClickListener(this)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        groceryViewModel.groceryListLive.observe(viewLifecycleOwner, Observer {
            lists.clear()
            lists = it.toMutableList()
            if (it.isNotEmpty()) groceryListsAdapter.updateItems(it)
        })
    }

    override fun onResume() {
        super.onResume()
        groceryViewModel.getAllLists()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_grocery_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_repeat -> {
                groceryViewModel.duplicateList(lists[0].id)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onGroceryListClicked(groceryList: GroceryList) {
        val args = Bundle().apply {
            putInt(KEY_LIST_ID, groceryList.id)
            putString(KEY_LIST_NAME, groceryList.name)
        }
        navController.navigate(R.id.groceryItemsFragment, args)
    }
}