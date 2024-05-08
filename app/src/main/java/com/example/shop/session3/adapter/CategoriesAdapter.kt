package com.example.shop.session3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ItemCartBinding
import com.example.shop.session3.data.Cart
import com.bumptech.glide.Glide
import com.example.shop.databinding.ItemCategoriesBinding


class CategoriesAdapter(private val context: Context, private val carts: List<Cart>) :
    RecyclerView.Adapter<CategoriesAdapter.CartViewHolder>() {

    private val uniqueCategories = HashSet<String>()

    init {
        for (cart in carts) {
            uniqueCategories.add(cart.category)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val category = uniqueCategories.elementAt(position)
        val cart = carts.find { it.category == category }
        cart?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = uniqueCategories.size

    inner class CartViewHolder(private val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: Cart) {
            with(binding) {

                name.text = cart.category
            }

        }
    }
}
