package com.kata3.kata3app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kata3.kata3app.databinding.ItemLayoutBinding

class ItemAdapter(
    private var itemList: List<ItemResponse>,
    private val onClickListener: (ItemResponse) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemResponse) {
            binding.tvItemName.text = item.name
            binding.root.setOnClickListener { onClickListener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    fun updateList(newList: List<ItemResponse>) {
        itemList = newList
        notifyDataSetChanged()
    }
}