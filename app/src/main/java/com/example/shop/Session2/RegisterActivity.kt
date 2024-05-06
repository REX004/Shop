package com.example.shop.Session2

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.shop.R
import com.example.shop.databinding.ActivityRegisterBinding
import com.example.shop.databinding.ActivitySignInBinding
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class RegisterActivity : AppCompatActivity() {

    private val supabaseUrl = "https://taiveobdxmijvagwftuv.supabase.co"
    private val supabaseKey =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InRhaXZlb2JkeG1panZhZ3dmdHV2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDY2OTEzOTMsImV4cCI6MjAyMjI2NzM5M30.CtF3QuD7OMEX1VgbBq4pqXbOMkrUt2jIxz6yOQ-6yt0"
    private lateinit var supabase: SupabaseClient
    private lateinit var binding: ActivityRegisterBinding

    @OptIn(SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Supabase client and auth
        supabase = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Auth)
            install(Realtime)
            install(Storage)
            install(Postgrest)
            httpConfig {
                Logging { this.level = LogLevel.BODY }
            }
        }

        // Set up password visibility toggle

        // Set up login button
        binding.logBT.setOnClickListener {
            val emailEntered = binding.emailET.text.toString()
            val passwordEntered = binding.passwordET.text.toString()

            if (emailEntered.isEmpty() || passwordEntered.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    supabase.auth.signUpWith(Email) {
                        email = emailEntered
                        password = passwordEntered
                        data = buildJsonObject {
                            put("first_name", binding.nameET.text.toString())
                        }
                    }
                }
            }
        }


        // Set up Google login button


        // Set up create user button
        binding.logTXT.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}