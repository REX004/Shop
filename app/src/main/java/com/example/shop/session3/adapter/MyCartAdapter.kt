package com.example.shop.session3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shop.databinding.ItemCartBinding
import com.example.shop.session3.data.Cart
import com.bumptech.glide.Glide
import com.example.shop.databinding.ItemMycartBinding
import com.example.shop.session3.MyCartActivity
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


class MyCartAdapter(private val context: Context, private val lifecycleOwner: LifecycleOwner, private val carts: List<Cart>) :
    RecyclerView.Adapter<MyCartAdapter.CartViewHolder>() {
    private val userId = "e426b8c7-95f8-4468-978c-a192d81a35ff"
    private val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
    private val supabaseKey =
        "SecretKey"

    private var itemTouchHelper: ItemTouchHelper? = null

    fun setItemTouchHelper(itemTouchHelper: ItemTouchHelper) {
        this.itemTouchHelper = itemTouchHelper
    }
    var count = 1
    private lateinit var supabase: SupabaseClient
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemMycartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cart = carts[position]
        holder.bind(cart)
    }

    override fun getItemCount(): Int = carts.size

    fun showQuantityControls(position: Int) {
        notifyItemChanged(position)
    }
    @SuppressLint("ClickableViewAccessibility")
    inner class CartViewHolder(private val binding: ItemMycartBinding) :
        RecyclerView.ViewHolder(binding.root) {


        init {

            binding.root.setOnTouchListener { _, event ->
                event?.let {
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        itemTouchHelper?.startSwipe(this@CartViewHolder)
                    }
                }
                false
            }

        }
        @SuppressLint("ClickableViewAccessibility")
        @OptIn(SupabaseInternal::class)
        fun bind(cart: Cart) {
            with(binding) {

                name.text = cart.name
                price.text = cart.price

                Glide.with(context)
                    .load(cart.picture)
                    .into(binding.crossIMG)
                supabase = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
                    install(Auth)
                    install(Realtime)
                    install(Storage)
                    install(Postgrest)
                    httpConfig {
                        Logging { this.level = LogLevel.BODY }
                    }
                }
                root.setOnTouchListener { _, event ->
                    event?.let {
                        if (event.action == MotionEvent.ACTION_DOWN) {
                            itemTouchHelper?.startSwipe(this@CartViewHolder)
                        }
                    }
                    false
                }

            }
        }

    }

}
