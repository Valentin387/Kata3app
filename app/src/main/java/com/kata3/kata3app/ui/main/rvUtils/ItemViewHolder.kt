package com.kata3.kata3app.ui.main.rvUtils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kata3.kata3app.data.DTO.ItemResponse
import com.kata3.kata3app.databinding.ItemLayoutBinding

class ItemViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemLayoutBinding.bind(view)

    fun render(
        itemModel: ItemResponse,
        onClickListener: (ItemResponse) -> Unit
    ) {
        binding.tvName.text = itemModel.title
        binding.tvDescription.text = itemModel.description ?: "No description"

        itemView.setOnClickListener {
            onClickListener(itemModel)
        }
    }
}