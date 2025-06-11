package com.kata3.kata3app.ui.main.rvUtils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kata3.kata3app.R
import com.kata3.kata3app.data.DTO.ItemResponse

class ItemAdapter(
        private var itemList: List<ItemResponse>,
        private val onClickListener: (ItemResponse) -> Unit
    ) :
    RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(
            layoutInflater.inflate(
                R.layout.item_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item, onClickListener)
    }

    fun updateList(newList: List<ItemResponse>) {
        val diffCallback = ItemDiffUtil(itemList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}