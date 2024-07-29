package com.rurouni.weatherapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.snackbar.Snackbar
import com.rurouni.weatherapp.ApiKey
import com.rurouni.weatherapp.adapter.ForecastListAdapter
import com.rurouni.weatherapp.databinding.ActivityMainBinding
import com.rurouni.weatherapp.service.WeatherApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val BASE_URL = "https://api.weatherapi.com/v1/"
val DAYS = "30"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter : ForecastListAdapter
    private lateinit var weatherApi: WeatherApi

    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION

    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        weatherApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(this)

        setLaunchers()
        reloadButtonListener()
        loadData()
    }

    private fun reloadButtonListener() {
        binding.reloadButton.setOnClickListener {
            loadData()
        }
    }

    private fun setLaunchers() {
        locationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->

            if (result) {
                //Get location
                loadData()
            }
            else {
                Toast.makeText(this, "Location is needed for fetch data", Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun checkPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(this, locationPermission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, locationPermission)) {
                Snackbar.make(binding.root, "Permission needed for fetching weather data!", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", View.OnClickListener {
                    locationPermissionLauncher.launch(locationPermission)
                }).show()
            }
            else {
                locationPermissionLauncher.launch(locationPermission)
            }
        }else {
            return true
        }

        return false
    }

    private fun startLoading() {
        binding.currentLoadingBar.visibility = View.VISIBLE
        binding.reloadButton.isClickable = false
        binding.infoText.visibility = View.GONE
    }

    private fun endLoading(info : Boolean) {
        binding.currentLoadingBar.visibility = View.GONE
        binding.reloadButton.isClickable = true

        if (info) {
            binding.infoText.visibility = View.VISIBLE
        }
        else {
            binding.infoText.visibility = View.GONE
        }
    }

    private fun loadData() {
        startLoading()
        if (checkPermission()) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                launch {
                    fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
                        override fun onCanceledRequested(listener: OnTokenCanceledListener) = CancellationTokenSource().token

                        override fun isCancellationRequested() = false
                    })
                        .addOnSuccessListener {
                            if (it == null) {
                                binding.infoText.text = "Location service must be opened"
                                endLoading(true)
                            }
                            else {
                                CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                                    val response = weatherApi.getForecast(ApiKey().KEY, "${it.latitude},${it.longitude}", DAYS)

                                    withContext(Dispatchers.Main) {
                                        if (response.isSuccessful) {

                                            if (response.body() != null) {
                                                adapter = ForecastListAdapter(response.body()!!)
                                                binding.forecastRecyclerView.adapter = adapter
                                                endLoading(false)
                                            }else {
                                                binding.infoText.text = "Response is null"
                                                endLoading(true)
                                            }
                                        }else {
                                            binding.infoText.text = "Response is unsuccessful"
                                            endLoading(true)
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }
        else {
            binding.infoText.text = "Location is needed for fetch data"
            endLoading(true)
        }
    }
}