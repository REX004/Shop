package com.example.shop.session3.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SwipeToDeleteCallback(private val adapter: MyCartAdapter) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private var viewHolder: RecyclerView.ViewHolder? = null

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        this.viewHolder = viewHolder
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.LEFT -> {
                if (adapter.isDeleteButtonVisible(position)) {
                    adapter.hideDeleteButton(position)
                } else {
                    adapter.showQuantityControls(position)
                }
            }
            ItemTouchHelper.RIGHT -> {
                if (adapter.isQuantityControlsVisible(position)) {
                    adapter.hideQuantityControls(position)
                } else {
                    adapter.showDeleteButton(position)
                }
            }
        }
        adapter.notifyItemChanged(position)
    }

}