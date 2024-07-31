package com.rurouni.weatherapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.rurouni.weatherapp.ViewModel.MainPageViewModel
import com.rurouni.weatherapp.ViewModel.MainPageViewModelFactory
import com.rurouni.weatherapp.adapter.ForecastListAdapter
import com.rurouni.weatherapp.databinding.FragmentMainPageViewBinding
import com.rurouni.weatherapp.service.WeatherApi
import com.rurouni.weatherapp.service.WeatherRepository

class MainPageView : Fragment() {
    private lateinit var binding: FragmentMainPageViewBinding
    private lateinit var viewModel: MainPageViewModel
    private lateinit var adapter: ForecastListAdapter
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>
    private val locationPermission = Manifest.permission.ACCESS_FINE_LOCATION
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherApi = WeatherApi.getInstance()
        val weatherRepository = WeatherRepository(weatherApi)
        viewModel = ViewModelProvider(this, MainPageViewModelFactory(weatherRepository)).get(MainPageViewModel::class.java)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        setLaunchers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainPageViewBinding.inflate(inflater, container, false)

        adapter = ForecastListAdapter()
        binding.forecastRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.forecastRecyclerView.adapter = adapter

        viewModel.weatherModel.observe(viewLifecycleOwner) { adapter.setModel(it) }

        viewModel.errorMessage.observe(viewLifecycleOwner) { Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show() }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.currentLoadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        getWeather()
        return binding.root
    }

    private fun getWeather() {
        if (checkPermission()) {
            viewModel.getWeather(fusedLocationClient, requireContext())
        } else {
            Toast.makeText(
                requireContext(),
                "Permission is required to fetch weather data",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setLaunchers() {
        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    viewModel.getWeather(fusedLocationClient, requireContext())
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Location is needed to fetch data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun checkPermission(): Boolean {
        return if (ContextCompat.checkSelfPermission(requireContext(), locationPermission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), locationPermission)) {
                Snackbar.make(
                    binding.root,
                    "Permission needed for fetching weather data!",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Give Permission") {
                    locationPermissionLauncher.launch(locationPermission)
                }.show()
            } else {
                locationPermissionLauncher.launch(locationPermission)
            }
            false
        } else {
            true
        }
    }
}
