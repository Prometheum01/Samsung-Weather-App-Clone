package com.rurouni.weatherapp.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.databinding.FragmentHomeBinding
import com.rurouni.weatherapp.utils.mapper.ForecastToDayItemList
import com.rurouni.weatherapp.utils.mapper.ForecastToHourlyList
import com.rurouni.weatherapp.ui.adapter.DayListAdapter
import com.rurouni.weatherapp.ui.adapter.HourlyListAdapter
import com.rurouni.weatherapp.ui.view_model.HomeViewModel
import com.rurouni.weatherapp.utils.ApiResultHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val hourlyListAdapter: HourlyListAdapter by lazy {
        HourlyListAdapter()
    }

    val dayListAdapter: DayListAdapter by lazy {
        DayListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHourlyRecyclerView()
        initDayRecyclerView()

        getForecast()
        observeForecastData()
    }

    private fun initHourlyRecyclerView() {
        binding.rwHours.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rwHours.adapter = hourlyListAdapter
    }

    private fun initDayRecyclerView() {
        binding.rwDays.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rwDays.adapter = dayListAdapter
    }

    private fun getForecast() {
        try {
             homeViewModel.getForecast()
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun observeForecastData() {
        try {
            homeViewModel.response.observe(viewLifecycleOwner) { response ->
                val apiResultHandler = ApiResultHandler<ForecastWeather>(requireContext(),
                    onLoading = {
                        // Show Progress
                        binding.loadingBar.visibility = View.VISIBLE
                    },
                    onSuccess = { data ->

                        data?.let {
                            binding.loadingBar.visibility = View.INVISIBLE
                            binding.tvCurrentTemperature.text = "${it.current?.temp_c}°C"
                            binding.tvHighTemperature.text = "${it.forecast.forecastday.first().day.maxtemp_c}°C"
                            binding.tvLowTemperature.text = "${it.forecast.forecastday.first().day.mintemp_c}°C"
                            binding.tvCurrentLocation.text = "${it.location.region}, ${it.location.name}, ${it.location.country}"

                            val hourlyItemList = ForecastToHourlyList(it.location, it.forecast)
                            hourlyListAdapter.setList(hourlyItemList)

                            val dayItemList = ForecastToDayItemList().invoke(it.forecast)
                            dayListAdapter.setList(dayItemList)
                        }
                    },
                    onFailure = {
                        binding.loadingBar.visibility = View.INVISIBLE
                    })
                apiResultHandler.handleApiResult(response)
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}