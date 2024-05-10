package com.example.shop.session3.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ItemCartBinding
import com.example.shop.session3.data.Cart
import com.bumptech.glide.Glide
import com.example.shop.databinding.ItemThumbnailBinding
import com.example.shop.session3.data.Favorite
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.util.Identity.decode
import kotlinx.coroutines.launch


class ThnumbailAdapter(private val context: Context, private val carts: List<Cart>) :
    RecyclerView.Adapter<ThnumbailAdapter.CartViewHolder>() {

    var count = 1
    private lateinit var supabase: SupabaseClient
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemThumbnailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = carts[position]
        holder.bind(cart)
    }

    override fun getItemCount(): Int = carts.size

    inner class CartViewHolder(private val binding: ItemThumbnailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @OptIn(SupabaseInternal::class)
        fun bind(cart: Cart) {

            Glide.with(context)
                .load(cart.picture)
                .into(binding.crossIMG)

        }

    }

}
