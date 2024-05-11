package com.example.shop.Session2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shop.databinding.ActivitySignInBinding
import com.example.shop.session3.BottomNavigationActivity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.launch


class SignInActivity : AppCompatActivity() {

    private val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
    private val supabaseKey =
        "SecretKey"

    private lateinit var supabase: SupabaseClient
    private lateinit var binding: ActivitySignInBinding

    @OptIn(SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
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

        binding.restoreTXT.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        binding.logBT.setOnClickListener {
            try {
                val emailEntered = binding.emailET.text.toString()
                val passwordEntered = binding.passwordET.text.toString()

                if (emailEntered.isEmpty() || passwordEntered.isEmpty()) {
                    Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                } else {
                    lifecycleScope.launch {
                        try {
                            val result = supabase.auth.signInWith(Email) {
                                email = emailEntered
                                password = passwordEntered
                            }
                            val user = result.hashCode()
                            if (user != null) {
                                // Sign-in successful, navigate to next screen
                                startActivity(Intent(this@SignInActivity, BottomNavigationActivity::class.java))
                            } else {
                                // Sign-in failed, show error message
                                Toast.makeText(this@SignInActivity, "Error: ${result.hashCode()?.dec()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            // Handle other exceptions
                            runOnUiThread {
                                Toast.makeText(this@SignInActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            } catch (e : HttpRequestException){
                runOnUiThread {
                    Log.e("Supabase", "${e.message}")
                    Toast.makeText(this@SignInActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        binding.createUsTXT.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}