package com.example.shop.session3

import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shop.databinding.HomeActivtyBinding
import com.example.shop.session3.adapter.CartAdapter

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

import com.example.shop.session3.adapter.CategoriesAdapter
import com.example.shop.session3.data.Cart


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: HomeActivtyBinding
    private val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl6anltcWtxdmhjdnlrbnJ4ZGdrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUwMDYyOTAsImV4cCI6MjAzMDU4MjI5MH0.REeIJC1YhC5t4KQW8F-HZenjFFRxgkhE2VfRv3xAWrY"

    private lateinit var supabase: SupabaseClient
    @OptIn(SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menuButton.setOnClickListener {
            binding.mainContainer.radius = 32f
            binding.mainContainer.pivotX = ((binding.mainContainer.width / 2).toFloat())
            binding.mainContainer.pivotY = ((binding.mainContainer.height / 2).toFloat())
            binding.mainContainer.scaleX = 0.71f
            binding.mainContainer.scaleY = 0.71f
            binding.mainContainer.translationX = 320f
            binding.mainContainer.rotation = -4f

            binding.mainContainer.setOnClickListener {
                binding.mainContainer.radius = 0f
                binding.mainContainer.pivotX = 0f
                binding.mainContainer.pivotY = 0f
                binding.mainContainer.scaleX = 1f
                binding.mainContainer.scaleY = 1f
                binding.mainContainer.translationX = 0f
                binding.mainContainer.rotation = 0f

                binding.mainContainer.setOnClickListener(null)
            }
        }

        val currenBritnes = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS)

        val oldBritnes = currenBritnes.toFloat() / 255.0f
        val imageCur = window.attributes
        val  imageset = 80
        binding.menuButton.setOnClickListener {
            val layoutparamsCurrent = window.attributes
            layoutparamsCurrent.screenBrightness = imageset / 100f
            window.attributes = layoutparamsCurrent
        }
        binding.back.setOnClickListener{
            val layOutParams = window.attributes
            layOutParams.screenBrightness = oldBritnes
            window.attributes = layOutParams
        }



//        binding.favoritesBT.setOnClickListener {
//            startActivity(Intent(this, FavoritesCartActivity::class.java))
//        }
        supabase = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Auth)
            install(Realtime)
            install(Storage)
            install(Postgrest)
            httpConfig {
                Logging { this.level = LogLevel.BODY }
            }
        }

//        binding.pdfView.fromAsset("one.pdf")
//
        lifecycleScope.launch {
            try {
                val city = supabase.from("products").select().decodeList<Cart>()
                Log.e("HomeActivity","Data loaded")
                val cartAdapter = CartAdapter(this@HomeActivity, this@HomeActivity, city)
//                binding.cartRV.adapter = cartAdapter
//                binding.cartRV.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
            } catch (e: Exception){
                showDialog("${e.message}")
            }
        }
//        lifecycleScope.launch {
//            try {
//                val city = supabase.from("products").select().decodeList<Cart>()
//                val categoryAdapter = CategoriesAdapter(this@HomeActivity, city)
//                binding.categoriesRV.adapter = categoryAdapter
//                binding.categoriesRV.layoutManager = LinearLayoutManager(this@HomeActivity, LinearLayoutManager.HORIZONTAL, false)
//            } catch (e: Exception){
//                showDialog("${e.message}")
//            }
//
//        }

        val shoes = listOf(
            Shoe(2),
            Shoe(4),
            Shoe(2),
            Shoe(5),
        )

        shoes.sortedByDescending { it.rating }.take(2)

//        println(shoes.map { it.category })
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

data class Shoe(
    val rating: Int
)