package com.kata3.kata3app.ui.main.newItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kata3.kata3app.R
import com.kata3.kata3app.data.DTO.ItemCreateRequest
import com.kata3.kata3app.data.repositories.ItemRepository
import com.kata3.kata3app.databinding.FragmentNewItemBinding
import com.kata3.kata3app.io.ItemService
import com.kata3.kata3app.utils.EncryptedPrefsManager

class NewItemFragment : Fragment() {

    private var _binding: FragmentNewItemBinding? = null
    private val binding get() = _binding!!

    private val itemService: ItemService by lazy {
        ItemService.create(requireContext())
    }

    private val newItemViewModel: NewItemViewModel by lazy {
        ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NewItemViewModel(ItemRepository(itemService)) as T
            }
        })[NewItemViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupSpinner() {
        val types = arrayOf("Project", "Task")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter
    }

    private fun setupClickListeners() {
        binding.btCreate.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val type = binding.spinnerType.selectedItem.toString().uppercase()

            if (validateInput(name)) {
                showLoadingSpinner()
                val request = ItemCreateRequest(name, type, description.takeIf { it.isNotEmpty() })
                newItemViewModel.createItem(fetchTokenFromPreferences(), request)
            } else {
                Toast.makeText(context, "Name is required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(name: String): Boolean {
        return name.isNotEmpty()
    }

    private fun observeViewModel() {
        newItemViewModel.createResult.observe(viewLifecycleOwner) { result ->
            hideLoadingSpinner()
            when (result) {
                is CreateItemResult.Success -> {
                    Toast.makeText(context, "Item created successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_newItemFragment_to_homeFragment)
                }
                is CreateItemResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchTokenFromPreferences(): String {
        val preferences = EncryptedPrefsManager.getPreferences()
        return preferences.getString("id_token", "") ?: ""
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