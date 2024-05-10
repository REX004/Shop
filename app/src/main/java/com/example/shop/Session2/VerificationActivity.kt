package com.example.shop.Session2

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.shop.R
import com.example.shop.databinding.ActivityVerificationBinding
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseExperimental
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
import kotlinx.coroutines.launch

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private lateinit var supabase: SupabaseClient
    private var isSending = false
    private lateinit var timer: CountDownTimer
    @OptIn(SupabaseExperimental::class, SupabaseInternal::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val supabaseUrl = "https://yzjymqkqvhcvyknrxdgk.supabase.co"
        val supabaseKey =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inl6anltcWtxdmhjdnlrbnJ4ZGdrIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTUwMDYyOTAsImV4cCI6MjAzMDU4MjI5MH0.REeIJC1YhC5t4KQW8F-HZenjFFRxgkhE2VfRv3xAWrY"

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
        binding.backIBT.setOnClickListener {
            onBackPressed()
        }
        timer = object : CountDownTimer(60000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = millisUntilFinished / 1000
                binding.timer.text = "00:$secondsUntilFinished"
            }

            override fun onFinish() {
                isSending = false
                binding.sendAgainTXT.text = "Отправить заново"
                binding.sendAgainTXT.isClickable = true
            }

        }
        timer.start()
        binding.sendAgainTXT.setOnClickListener {
            if (!isSending){
                isSending = true
                binding.sendAgainTXT.isClickable = false
                timer.start()
            }
        }

        val editTexts = arrayOf(
            binding.otp1,
            binding.otp2,
            binding.otp3,
            binding.otp4,
            binding.otp5,
            binding.otp6)
        for(i in editTexts.indices){
            val currentEditText = editTexts[i]
            currentEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < editTexts.lastIndex){
                        editTexts[i + 1].requestFocus()
                    }
                }

            })
        }

    }
}