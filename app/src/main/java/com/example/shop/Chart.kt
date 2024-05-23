package com.example.shop

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ItemCartBinding
import com.example.shop.session3.data.Cart

class Chart(val context : Context, val carts : List<Cart>): RecyclerView.Adapter<Chart.Holder>()  {
    inner class Holder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(cart: Cart) = with(binding){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCartBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
    return Holder(binding)
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val carts = carts[position]
        return holder.bind(carts)
    }
}