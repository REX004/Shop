package com.example.shop.session1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager.widget.ViewPager
import com.example.shop.R
import com.example.shop.Session2.SignInActivity
import com.example.shop.databinding.ActivityOnBoardingBinding
import com.example.shop.databinding.DialogBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var adapter: OnBoardingAdapter

    private val imageResources = intArrayOf(R.drawable.screen1tool, R.drawable.screen2tool, R.drawable.screen3tool)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = OnBoardingAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = adapter

        binding.buttonNext.setOnClickListener {
            if (binding.viewPager.currentItem == 2) {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            } else {
                binding.viewPager.currentItem += 1
            }
        }
        var bindingDialog = DialogBinding.inflate(layoutInflater)

        updateButtonText()
        updateNavImage()

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                updateButtonText()
                updateNavImage()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    private fun updateButtonText() {
        binding.buttonNext.text = when (binding.viewPager.currentItem) {
            0 -> "Начать"
            1 -> "Далее"
            2 -> "Далее"
            else -> "Далее"
        }
    }

    private fun updateNavImage() {
        binding.navIMG.setImageResource(imageResources[binding.viewPager.currentItem])
    }
}