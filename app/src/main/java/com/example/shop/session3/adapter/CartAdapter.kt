package com.example.shop.session3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ItemCartBinding
import com.example.shop.session3.data.Cart
import com.bumptech.glide.Glide


class CartAdapter(private val context: Context, private val carts: List<Cart>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = carts[position]
        holder.bind(cart)
    }

    override fun getItemCount(): Int = carts.size

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: Cart) {
            with(binding) {

                name.text = cart.name
                price.text = cart.price
            }

            Glide.with(context)
                .load(cart.picture)
                .into(binding.crossIMG)
        }
    }
}
