package com.example.shop.session3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.databinding.ActivityFavoritesCartBinding
import com.example.shop.databinding.HomeActivtyBinding
import com.example.shop.session3.adapter.CartFavotiesAdapter
import com.example.shop.session3.data.Favorite
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

class FavoritesCartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoritesCartBinding
    private val userId = "e426b8c7-95f8-4468-978c-a192d81a35ff"

    private val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl6anltcWtxdmhjdnlrbnJ4ZGdrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUwMDYyOTAsImV4cCI6MjAzMDU4MjI5MH0.REeIJC1YhC5t4KQW8F-HZenjFFRxgkhE2VfRv3xAWrY"

    private lateinit var supabase: SupabaseClient
    @OptIn(SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesCartBinding.inflate(layoutInflater)
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
                val city = supabase.from("favorites").select{
                    filter {
                        eq("user_id", userId)
                    }
                }
                    .decodeList<Favorite>()
                Log.e("HomeActivity","Data loaded")
                val CartFavotiesAdapter = CartFavotiesAdapter(this@FavoritesCartActivity, this@FavoritesCartActivity, city)
                binding.favouriteRV.adapter = CartFavotiesAdapter
                binding.favouriteRV.layoutManager = GridLayoutManager(this@FavoritesCartActivity, 2)
            } catch (e: Exception){
                showDialog("${e.message}")
                Log.e("HomeActivity","Data: ${e.message}")

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