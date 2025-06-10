package com.kata3.kata3app.ui.main.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kata3.kata3app.R
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.data.DTO.ItemUpdateRequest
import com.kata3.kata3app.databinding.FragmentDetailsBinding
import com.kata3.kata3app.io.ItemService
import com.kata3.kata3app.utils.EncryptedPrefsManager

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val itemService: ItemService by lazy {
        ItemService.create(requireContext())
    }

    private val detailsViewModel: DetailsViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailsViewModel(ItemRepository(itemService)) as T
            }
        })[DetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemId = arguments?.getString("itemId") ?: return

        setupClickListeners()
        showLoadingSpinner()
        detailsViewModel.fetchItem(fetchTokenFromPreferences(), itemId)
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btUpdate.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val itemId = arguments?.getString("itemId") ?: return@setOnClickListener

            if (name.isNotEmpty() || description.isNotEmpty()) {
                showLoadingSpinner()
                val request = ItemUpdateRequest(
                    name.takeIf { it.isNotEmpty() },
                    description.takeIf { it.isNotEmpty() }
                )
                detailsViewModel.updateItem(fetchTokenFromPreferences(), itemId, request)
            } else {
                Toast.makeText(context, "At least one field must be updated.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btDelete.setOnClickListener {
            val itemId = arguments?.getString("itemId") ?: return@setOnClickListener
            showLoadingSpinner()
            detailsViewModel.deleteItem(fetchTokenFromPreferences(), itemId)
        }
    }

    private fun observeViewModel() {
        detailsViewModel.item.observe(viewLifecycleOwner) { item ->
            hideLoadingSpinner()
            if (item != null) {
                binding.etName.setText(item.name)
                binding.etDescription.setText(item.description ?: "")
                binding.tvCreatedAt.text = "Created: ${item.created_at}"
            }
        }

        detailsViewModel.result.observe(viewLifecycleOwner) { result ->
            hideLoadingSpinner()
            when (result) {
                is DetailsResult.Success -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                    if (result.isDeleted) {
                        findNavController().navigate(R.id.action_detailsFragment_to_homeFragment)
                    }
                }
                is DetailsResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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