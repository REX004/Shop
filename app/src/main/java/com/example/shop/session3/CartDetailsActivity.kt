package com.example.shop.session3

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shop.R
import com.example.shop.databinding.ActivityCartDetailsBinding
import com.example.shop.databinding.HomeActivtyBinding
import com.example.shop.session3.adapter.CartAdapter
import com.example.shop.session3.adapter.ImagePagerAdapter
import com.example.shop.session3.adapter.ThnumbailAdapter
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

class CartDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartDetailsBinding
    private val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
    private val supabaseKey =
        "SecretKey"

    private lateinit var supabase: SupabaseClient
    @OptIn(SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartDetailsBinding.inflate(layoutInflater)
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
                val cartAdapter = ThnumbailAdapter(this@CartDetailsActivity, city)
                binding.detailsRV.adapter = cartAdapter
                binding.detailsRV.layoutManager = LinearLayoutManager(this@CartDetailsActivity, LinearLayoutManager.HORIZONTAL, false)
            } catch (e: Exception){
                showDialog("${e.message}")
            }
        }

        lifecycleScope.launch {
            try {
                val city = supabase.from("products").select().decodeList<Cart>()
                Log.e("HomeActivity","Data loaded")
                val cartAdapter = ImagePagerAdapter(this@CartDetailsActivity, city)
                binding.crossMain.adapter = cartAdapter
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