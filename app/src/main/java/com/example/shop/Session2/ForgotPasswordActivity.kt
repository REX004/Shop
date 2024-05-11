package com.example.shop.Session2

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.shop.R
import com.example.shop.databinding.ActivityForgotPasswordBinding
import com.google.android.material.button.MaterialButton
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.OtpType
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.OTP
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var supabase: SupabaseClient

    @OptIn(SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
        val supabaseKey =
            "SecretKey"
        // todo создание supabase клиента
        supabase = createSupabaseClient(supabaseUrl = supabaseUrl, supabaseKey = supabaseKey) {
            install(Auth)
            install(Realtime)
            install(Storage)
            install(Postgrest)
            httpConfig {
                Logging { this.level = LogLevel.BODY }
            }

        }

        binding.logBT.setOnClickListener {
            lifecycleScope.launch {
                showDialog()
                delay(3000)
                try {
                    val emailET = binding.emailET.text.toString()
                    lifecycleScope.launch {
                        supabase.auth.resendEmail(OtpType.Email.EMAIL_CHANGE, emailET)
                        runOnUiThread {
                            Log.e("ForgotPassword", "OTP CODE SEND")
                        }

                        startActivity(Intent(this@ForgotPasswordActivity, VerificationActivity::class.java))
                        finish()
                    }
                } catch (e: Exception){
                    Toast.makeText(this@ForgotPasswordActivity, e.message, Toast.LENGTH_SHORT).show()
                    Log.e("ForgotPassword", "${e.message}")

                }


            }
        }


    }
    private fun showDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.dialog_layout)

        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


    }
}