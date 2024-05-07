package com.example.shop.session1

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.shop.R
import com.example.shop.session1.fragments.Screen1Fragment
import com.example.shop.session1.fragments.Screen2Fragment
import com.example.shop.session1.fragments.Screen3Fragment
class OnBoardingAdapter(private val context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> Screen1Fragment()
            1 -> Screen2Fragment()
            2 -> Screen3Fragment()
            else -> throw RuntimeException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 3
    }
}