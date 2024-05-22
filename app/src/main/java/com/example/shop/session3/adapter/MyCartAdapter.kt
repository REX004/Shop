package com.example.shop.session3.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.R
import com.example.shop.databinding.ItemMycartBinding
import com.example.shop.session3.data.Cart

class MyCartAdapter(private val carts: List<Cart>) :
    RecyclerView.Adapter<MyCartAdapter.CartViewHolder>() {

    private var itemTouchHelper: ItemTouchHelper? = null
    private val deletedItems = mutableListOf<Int>()
    private val quantityControls = mutableMapOf<Int, Boolean>()

    fun setItemTouchHelper(itemTouchHelper: ItemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper
    }

    fun isDeleteButtonVisible(position: Int): Boolean {
        return deletedItems.contains(position)
    }

    fun isQuantityControlsVisible(position: Int): Boolean {
        return quantityControls[position] == true
    }

    fun hideDeleteButton(position: Int) {
        deletedItems.remove(position)
        notifyItemChanged(position)
    }

    fun hideQuantityControls(position: Int) {
        quantityControls.remove(position)
        notifyItemChanged(position)
    }

    fun showDeleteButton(position: Int) {
        quantityControls.remove(position)
        deletedItems.add(position)
        notifyItemChanged(position)
    }

    fun showQuantityControls(position: Int) {
        deletedItems.remove(position)
        quantityControls[position] = true
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemMycartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = carts[position]
        holder.bind(cart)

        val isDeleteButtonVisible = isDeleteButtonVisible(position)
        val isQuantityControlsVisible = isQuantityControlsVisible(position)

        holder.itemView.findViewById<View>(R.id.plusMinusCardView).visibility =
            if (isDeleteButtonVisible) View.VISIBLE else View.GONE
        holder.itemView.findViewById<View>(R.id.deleteCardView).visibility =
            if (isQuantityControlsVisible) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = carts.size

    @SuppressLint("ClickableViewAccessibility")
    inner class CartViewHolder(private val binding: ItemMycartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnTouchListener { _, event ->
                if (event?.action == MotionEvent.ACTION_DOWN) {
                    itemTouchHelper?.startSwipe(this@CartViewHolder)
                }
                false
            }
        }

        fun bind(cart: Cart) {
            with(binding) {
                name.text = cart.name
                price.text = cart.price

                Glide.with(itemView)
                    .load(cart.picture)
                    .into(binding.crossIMG)
            }
        }
    }
}