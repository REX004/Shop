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


class CartAdapter(private val context: Context, private val lifecycleOwner: LifecycleOwner, private val carts: List<Cart>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
        private val userId = "e426b8c7-95f8-4468-978c-a192d81a35ff"
    private val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl6anltcWtxdmhjdnlrbnJ4ZGdrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUwMDYyOTAsImV4cCI6MjAzMDU4MjI5MH0.REeIJC1YhC5t4KQW8F-HZenjFFRxgkhE2VfRv3xAWrY"


    var count = 1
    private lateinit var supabase: SupabaseClient
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



                addfavouriteIMG.setOnClickListener {
                    lifecycleOwner.lifecycleScope.launch {

                        val existingFavorites = try {
                            supabase.from("favorites").select(columns = Columns.list("id")){
                                filter {
                                    eq("user_id", userId)
                                    eq("picture", cart.picture)
                                }
                            }.decodeList<Favorite>()
                        } catch (e: Exception){
                            e.printStackTrace()
                            emptyList<Favorite>()
                        }
                        if (existingFavorites.isNotEmpty()){
                            try {
                                supabase.from("favorites")
                                    .delete {
                                        filter {
                                            eq("id", existingFavorites.first().id!!)
                                            eq("user_id", userId)
                                        }
                                    }
                                Log.e("CartDAPTER", "INFORMATION DELETED")
                            } catch (e: Exception){
                                e.printStackTrace()
                                Log.e("CartDAPTER", "INFORMATION DELETED FAIL")

                            }


                        }
                        else {
                            val favorites = Favorite(
                                id = null,
                                user_id = userId,
                                picture = cart.picture,
                                price = cart.price,
                                information = cart.information,
                                gender = cart.gender,
                                category = cart.category,
                                name = cart.name
                            )
                            try {
                                supabase.from("favorites")
                                    .insert(favorites)

                                Log.e("CartDAPTER", "INFORMATION LOAD")

                            } catch (e: Exception){
                                e.printStackTrace()
                                Log.e("CartDAPTER", "INFORMATION LOAD FAILED: ${e.message}")
                            }
                        }




                    }
                }

                plusIMG.setOnClickListener {
                    lifecycleOwner.lifecycleScope.launch {

                        val existingFavorites = try {
                            supabase.from("busket").select(columns = Columns.list("id")){
                                filter {
                                    eq("user_id", userId)
                                    eq("picture", cart.picture)
                                }
                            }.decodeList<Favorite>()
                        } catch (e: Exception){
                            e.printStackTrace()
                            emptyList<Favorite>()
                        }
                        if (existingFavorites.isEmpty()) {
                            val favorites = Favorite(
                                id = count,
                                user_id = userId,
                                picture = cart.picture,
                                price = cart.price,
                                information = cart.information,
                                gender = cart.gender,
                                category = cart.category,
                                name = cart.name
                            )
                            count += 1
                            try {
                                supabase.from("busket")
                                    .insert(favorites)

                                Log.e("CartDAPTER", "INFORMATION LOAD")

                            } catch (e: Exception){
                                e.printStackTrace()
                                Log.e("CartDAPTER", "INFORMATION LOAD FAILED: ${e.message}")
                            }
                        }




                    }
                }
            }
        }

    }

}
