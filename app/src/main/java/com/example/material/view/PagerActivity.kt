package com.example.material.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.material.databinding.PagerActivityBinding
import com.example.material.model.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class PagerActivity : AppCompatActivity() {
    private lateinit var binding: PagerActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PagerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Earth"
                1 -> tab.text = "Mars"
                2 -> tab.text = "Weather"
            }
        }.attach()

    }
}