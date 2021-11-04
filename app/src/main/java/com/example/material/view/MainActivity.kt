package com.example.material.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.material.R
import com.example.material.databinding.MainActivityBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    private val fragmentPictureOfTheDay by lazy { PictureOfTheDayFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragmentPictureOfTheDay)
            .commit()
    }
}