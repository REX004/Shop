package com.example.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.shop.Session2.SignInActivity
import com.example.shop.databinding.ActivityOnBoardingBinding
import com.example.shop.session1.OnBoardingAdapter
import com.example.shop.session1.fragments.Screen1Fragment
import com.example.shop.session1.fragments.Screen2Fragment
import com.example.shop.session1.fragments.Screen3Fragment

class TrainingActivity: AppCompatActivity(){
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
            if (binding.viewPager.currentItem == 2){
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            } else binding.viewPager.currentItem += 1
        }
        updateButton()
        updateNavItem()

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                updateNavItem()
                updateButton()
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }
    private fun updateButton(){
        binding.buttonNext.text = when (binding.viewPager.currentItem){
            0 -> "Начать"
            1 -> "Далее"
            2 -> "Далее"
            else -> "Далее"
        }
    }
    private fun updateNavItem(){
        binding.navIMG.setImageResource(imageResources[binding.viewPager.currentItem])
    }
}
//class TrainingActivity(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm){
//    override fun getCount(): Int = 3
//
//    override fun getItem(position: Int): Fragment {
//        return when(position) {
//            0 -> Screen1Fragment()
//            1 -> Screen2Fragment()
//            2 -> Screen3Fragment()
//            else -> throw RuntimeException("Invalid position")
//        }
//    }
//
//}

//class TrainingActivity: AppCompatActivity() {
//    private lateinit var binding: ActivityOnBoardingBinding
//    private lateinit var adapter: OnBoardingAdapter
//
//    private val imageResources = intArrayOf(R.drawable.screen1tool, R.drawable.screen2tool, R.drawable.screen3tool)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        adapter = OnBoardingAdapter(this, supportFragmentManager)
//        binding.viewPager.adapter = adapter
//
//        binding.buttonNext.setOnClickListener {
//
//
//            if (binding.viewPager.currentItem == 2){
//                val intent = Intent(this, SignInActivity::class.java)
//                startActivity(intent)
//            }else{
//                binding.viewPager.currentItem += 1
//            }
//        }
//        updateButtonText()
//        updateNavItem()
//
//        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//            }
//
//            override fun onPageSelected(position: Int) {
//                updateNavItem()
//                updateButtonText()
//            }
//
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//        })
//    }
//    private fun updateButtonText(){
//        binding.buttonNext.text = when (binding.viewPager.currentItem){
//            0 -> "Начать"
//            1 -> "Далее"
//            2 -> "Далее"
//            else -> "Далее"
//        }
//    }
//    private fun updateNavItem(){
//        binding.navIMG.setImageResource(imageResources[binding.viewPager.currentItem])
//    }
//}