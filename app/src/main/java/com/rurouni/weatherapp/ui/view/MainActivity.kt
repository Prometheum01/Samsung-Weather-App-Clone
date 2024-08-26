package com.rurouni.weatherapp.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rurouni.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

val BASE_URL = "https://api.weatherapi.com/v1/"
val KEY = "17abb3d06fd24ea4a2c111142242907"

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

}