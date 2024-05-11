package com.example.shop.session3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.databinding.ActivityMyCartBinding
import com.example.shop.databinding.HomeActivtyBinding
import com.example.shop.session3.adapter.CartAdapter
import com.example.shop.session3.adapter.CategoriesAdapter
import com.example.shop.session3.adapter.MyCartAdapter
import com.example.shop.session3.adapter.SwipeToDeleteCallback
import com.example.shop.session3.data.Cart
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.launch

class MyCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyCartBinding
    private val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl6anltcWtxdmhjdnlrbnJ4ZGdrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUwMDYyOTAsImV4cCI6MjAzMDU4MjI5MH0.REeIJC1YhC5t4KQW8F-HZenjFFRxgkhE2VfRv3xAWrY"

    private lateinit var supabase: SupabaseClient
    @OptIn(SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyCartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supabase = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Auth)
            install(Realtime)
            install(Storage)
            install(Postgrest)
            httpConfig {
                Logging { this.level = LogLevel.BODY }
            }
        }
        lifecycleScope.launch {
            try {
                val city = supabase.from("products").select().decodeList<Cart>()
                Log.e("HomeActivity","Data loaded")
                val cartAdapter = MyCartAdapter(this@MyCartActivity, this@MyCartActivity, city)
                binding.cartRV.adapter = cartAdapter
                binding.cartRV.layoutManager = LinearLayoutManager(this@MyCartActivity, LinearLayoutManager.VERTICAL, false)

                val swipeToDeleteCallback = SwipeToDeleteCallback(cartAdapter)
                val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
                itemTouchHelper.attachToRecyclerView(binding.cartRV)
                cartAdapter.setItemTouchHelper(itemTouchHelper)
            } catch (e: Exception){
                showDialog("${e.message}")
            }
        }


    }
    private fun showDialog(message: String){
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("Try again") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}