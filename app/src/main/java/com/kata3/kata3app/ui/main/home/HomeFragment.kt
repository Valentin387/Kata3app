package com.kata3.kata3app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kata3.kata3app.R
import com.kata3.kata3app.data.repositories.ItemRepository
import com.kata3.kata3app.databinding.FragmentHomeBinding
import com.kata3.kata3app.io.ItemService
import com.kata3.kata3app.utils.EncryptedPrefsManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var llmanager: LinearLayoutManager
    private lateinit var adapter: ItemAdapter

    private val itemService: ItemService by lazy {
        ItemService.create(requireContext())
    }

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(ItemRepository(itemService)) as T
            }
        })[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemType = arguments?.getString("type") ?: "PROJECT"
        val token = fetchTokenFromPreferences()

        showLoadingSpinner()
        homeViewModel.fetchItems(token, itemType)

        initRecyclerView()
        setupClickListeners()
    }

    private fun initRecyclerView() {
        llmanager = LinearLayoutManager(context)
        val itemList = homeViewModel.itemList.value ?: emptyList()

        adapter = ItemAdapter(
            itemList = itemList,
            onClickListener = { item -> onItemSelected(item) }
        )

        val decoration = DividerItemDecoration(context, llmanager.orientation)
        binding.rvItems.layoutManager = llmanager
        binding.rvItems.adapter = adapter
        binding.rvItems.addItemDecoration(decoration)

        homeViewModel.itemList.observe(viewLifecycleOwner) { items ->
            hideLoadingSpinner()
            adapter.updateList(items)
        }
    }

    private fun setupClickListeners() {
        binding.btAddNew.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newItemFragment)
        }
    }

    private fun onItemSelected(item: ItemResponse) {
        val bundle = Bundle().apply { putString("itemId", item.id) }
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
    }

    private fun fetchTokenFromPreferences(): String {
        val preferences = EncryptedPrefsManager.getPreferences()
        return preferences.getString("jwt_token", "") ?: ""
    }

    private fun showLoadingSpinner() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoadingSpinner() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}