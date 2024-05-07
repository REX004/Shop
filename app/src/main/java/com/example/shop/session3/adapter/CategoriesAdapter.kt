package com.example.shop.session3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.R
import com.example.shop.databinding.ItemCategoriesBinding
import com.google.android.material.card.MaterialCardView


class CategoriesAdapter(private val items: List<String>) :
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
        private lateinit var binding: ItemCategoriesBinding

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView = itemView.findViewById(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categories, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

