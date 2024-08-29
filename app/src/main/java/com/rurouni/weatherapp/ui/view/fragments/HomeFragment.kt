package com.rurouni.weatherapp.ui.view.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.rurouni.weatherapp.data.source.remote.model.Forecast
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.databinding.FragmentHomeBinding
import com.rurouni.weatherapp.ui.adapter.AdapterAnimation
import com.rurouni.weatherapp.ui.adapter.DayListAdapter
import com.rurouni.weatherapp.ui.adapter.HourlyListAdapter
import com.rurouni.weatherapp.ui.model.ColorPalette
import com.rurouni.weatherapp.ui.model.ColorState
import com.rurouni.weatherapp.ui.model.ColorTheme
import com.rurouni.weatherapp.ui.view_model.HomeViewModel
import com.rurouni.weatherapp.utils.ApiResultHandler
import com.rurouni.weatherapp.utils.ListConverters
import com.rurouni.weatherapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val hourlyListAdapter: HourlyListAdapter by lazy {
        HourlyListAdapter(binding.rwHours)
    }

    val dayListAdapter: DayListAdapter by lazy {
        DayListAdapter()
    }

    private lateinit var mainState : ColorState
    private lateinit var systemState : ColorState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        mainState = ColorState(ColorPalette.mainPalette(requireContext()), ColorPalette.systemPalette(requireContext()), ColorTheme.MAIN)
        systemState = ColorState(ColorPalette.systemPalette(requireContext()), ColorPalette.mainPalette(requireContext()), ColorTheme.SYSTEM)

        appBarObserver()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViews()

        getForecast()
        observeForecastData()
    }

    private fun initRecyclerViews() {
        binding.rwHours.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rwHours.adapter = hourlyListAdapter
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
            homeViewModel.currentForecast.observe(viewLifecycleOwner) { response ->
                val apiResultHandler = ApiResultHandler<ForecastWeather>(requireContext(),
                    onLoading = {
                        onLoading()
                    },
                    onSuccess = { data ->
                        onSuccess(data)
                    },
                    onFailure = {
                        onFailure()
                    })
                apiResultHandler.handleApiResult(response)
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    private fun onLoading() {
        binding.loadingBar.visibility = View.VISIBLE
    }

    private fun onSuccess(data : ForecastWeather?) {
        data?.let {
            binding.loadingBar.visibility = View.INVISIBLE
            binding.tvCurrentTemperature.text = "${it.current?.temp_c}°"
            binding.tvDetailTemperature.text = "${it.forecast.forecastday.first().day.maxtemp_c}° / ${it.forecast.forecastday.first().day.mintemp_c}°, Feels like ${it.current.feelslike_c}°"
            binding.tvCurrentLocation.text = "${it.location.name}, ${it.location.country}"
            binding.tvCondition.text = it.current.condition.text

            binding.tvCollapseCurrentTemp.text = "${it.current?.temp_c}°"
            binding.tvCollapseDetailTemp.text = "${it.forecast.forecastday.first().day.maxtemp_c}° / ${it.forecast.forecastday.first().day.mintemp_c}°"
            binding.tvCollapseCondition.text =  it.current.condition.text

            binding.tvUvValue.text = it.current.uv.toString()
            binding.tvHumidityValue.text = "%${it.current.humidity}"
            binding.tvWindValue.text = "${it.current.wind_kph} kph"
            binding.tvSunriseValue.text = it.forecast.forecastday.first().astro.sunrise
            binding.tvSunsetValue.text = it.forecast.forecastday.first().astro.sunset

            val code = Utils.codeToIconId(requireContext(), it.current.condition.code)
            code?.let {
                binding.imgCurrentCondition.setImageResource(it)
                binding.imgCollapseCondition.setImageResource(it)
            }

            val hourlyItemList = ListConverters.forecastToHourlyItem(it.location, it.forecast)
            hourlyListAdapter.setList(hourlyItemList)
            hourlyListAdapter.setColorState(mainState)

            val dayItemList = ListConverters.forecastToDayItem(it.forecast)
            dayListAdapter.setList(dayItemList)
            dayListAdapter.setColorState(mainState)
        }
    }

    private fun onFailure() {
        binding.loadingBar.visibility = View.INVISIBLE
    }

    private fun appBarObserver() {
        val appBarLayout = binding.appbar
        val expandedContent = binding.layoutExpand
        val toolbarContent = binding.layoutToolbar

        var animateFlag = false

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val progress = Math.abs(verticalOffset / totalScrollRange.toFloat())

            expandedContent.alpha = 1 - (progress * 2)
            toolbarContent.alpha = (progress * 2)

            if (progress > 0.4) {
                if (!animateFlag) {
                    changePalette(mainState, systemState)
                    animateFlag = true
                }
                if (progress >= 1.0) {
                    toolbarContent.setBackgroundColor(systemState.currentPalette.primary)
                }else if (progress <= 0.9) {
                    toolbarContent.setBackgroundColor(Color.TRANSPARENT)
                }
            }
            else {
                if (animateFlag) {
                    changePalette(systemState, mainState)
                    animateFlag = false
                }
            }
        })
    }

    private fun changePalette(current: ColorState, next : ColorState) {
        with(current) {
            AdapterAnimation.animateBackgroundColor(binding.layoutMain, currentPalette.primary, nextPalette.primary)

            //Cards
            AdapterAnimation.animateCardViewBackgroundColor(binding.cardHourlyList, currentPalette.secondary, nextPalette.secondary)
            AdapterAnimation.animateCardViewBackgroundColor(binding.cardDaysList, currentPalette.secondary, nextPalette.secondary)
            AdapterAnimation.animateCardViewBackgroundColor(binding.cardUv, currentPalette.secondary, nextPalette.secondary)
            AdapterAnimation.animateCardViewBackgroundColor(binding.cardHumidity, currentPalette.secondary, nextPalette.secondary)
            AdapterAnimation.animateCardViewBackgroundColor(binding.cardWind, currentPalette.secondary, nextPalette.secondary)
            AdapterAnimation.animateCardViewBackgroundColor(binding.cardSuntime, currentPalette.secondary, nextPalette.secondary)

            //Card Values
            AdapterAnimation.animateImageViewTintColorChange(binding.imgUv, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateImageViewTintColorChange(binding.imgHumidity, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateImageViewTintColorChange(binding.imgWind, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateImageViewTintColorChange(binding.imgSunrise, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateImageViewTintColorChange(binding.imgSunset, currentPalette.onPrimary, nextPalette.onPrimary)

            AdapterAnimation.animateTextColorChange(binding.tvUvTitle, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateTextColorChange(binding.tvUvValue, currentPalette.onSecondary, nextPalette.onSecondary)

            AdapterAnimation.animateTextColorChange(binding.tvHumidityTitle, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateTextColorChange(binding.tvHumidityValue, currentPalette.onSecondary, nextPalette.onSecondary)

            AdapterAnimation.animateTextColorChange(binding.tvWindTitle, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateTextColorChange(binding.tvWindValue, currentPalette.onSecondary, nextPalette.onSecondary)

            AdapterAnimation.animateTextColorChange(binding.tvSunriseTitle, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateTextColorChange(binding.tvSunriseValue, currentPalette.onSecondary, nextPalette.onSecondary)

            AdapterAnimation.animateTextColorChange(binding.tvSunsetTitle, currentPalette.onPrimary, nextPalette.onPrimary)
            AdapterAnimation.animateTextColorChange(binding.tvSunsetValue, currentPalette.onSecondary, nextPalette.onSecondary)

            AdapterAnimation.animateTextColorChange(binding.tvHourly, currentPalette.onPrimary, nextPalette.onPrimary)

        }
        with(next) {
            hourlyListAdapter.animate(next)
            dayListAdapter.animate(next)
        }
    }
}