package com.kata3.kata3app.ui.main.rvUtils

import androidx.recyclerview.widget.DiffUtil
import com.kata3.kata3app.data.DTO.ItemResponse

class ItemDiffUtil (
    private val oldList : List<ItemResponse>,
    private val newList : List<ItemResponse>
) : DiffUtil.Callback () {


    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}