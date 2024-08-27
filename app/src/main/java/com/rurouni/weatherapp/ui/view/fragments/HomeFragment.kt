package com.rurouni.weatherapp.ui.view.fragments

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.rurouni.weatherapp.R
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.databinding.FragmentHomeBinding
import com.rurouni.weatherapp.utils.mapper.ForecastToDayItemList
import com.rurouni.weatherapp.utils.mapper.ForecastToHourlyList
import com.rurouni.weatherapp.ui.adapter.DayListAdapter
import com.rurouni.weatherapp.ui.adapter.HourlyListAdapter
import com.rurouni.weatherapp.ui.view_model.HomeViewModel
import com.rurouni.weatherapp.utils.ApiResultHandler
import com.rurouni.weatherapp.utils.Utils
import com.rurouni.weatherapp.utils.Utils.fadeIn
import com.rurouni.weatherapp.utils.Utils.fadeOut
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
        appBarObserver()
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
                            binding.tvCurrentTemperature.text = "${it.current?.temp_c}°"
                            binding.tvDetailTemperature.text = "${it.forecast.forecastday.first().day.maxtemp_c}° / ${it.forecast.forecastday.first().day.mintemp_c}°, Feels like ${it.current.feelslike_c}°"
                            binding.tvCurrentLocation.text = "${it.location.name}, ${it.location.country}"
                            binding.tvCondition.text = it.current.condition.text

                            binding.tvCollapseCurrentTemp.text = "${it.current?.temp_c}°"
                            binding.tvCollapseDetailTemp.text = "${it.forecast.forecastday.first().day.maxtemp_c}° / ${it.forecast.forecastday.first().day.mintemp_c}°"
                            binding.tvCollapseCondition.text =  it.current.condition.text


                            val code = Utils.codeToIconId(requireContext(), it.current.condition.code)
                            code?.let {
                                binding.imgCurrentCondition.setImageResource(it)
                                binding.imgCollapseCondition.setImageResource(it)
                            }

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

    private fun appBarObserver() {
        val appBarLayout = binding.appbar
        val expandedContent = binding.layoutExpand
        val toolbarContent = binding.layoutToolbar

        val defaultColor = ContextCompat.getColor(requireContext(), R.color.blue)
        val collapseColor = ContextCompat.getColor(requireContext(), R.color.g_white)

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val progress = Math.abs(verticalOffset / totalScrollRange.toFloat()) // Kaydırma oranını hesapla (0.0 ile 1.0 arasında)

            // Layoutların görünürlüğünü ve saydamlığını ayarla
            expandedContent.alpha = 1 - progress // Yukarı kaydırıldıkça görünürlüğü azalır
            toolbarContent.alpha = progress // Yukarı kaydırıldıkça görünürlüğü artar

            // Renk geçişini ayarla
            val blendedColor = ArgbEvaluator().evaluate(progress, defaultColor, collapseColor) as Int
            binding.layoutMain.setBackgroundColor(blendedColor)
            binding.layoutToolbar.setBackgroundColor(blendedColor)

            // Tamamen collapse olduğunda expandedContent görünmez ve toolbarContent görünür hale gelir
            if (progress >= 0.5) {
                expandedContent.visibility = View.VISIBLE
                toolbarContent.visibility = View.VISIBLE

                if (progress >= 1.0) {
                    expandedContent.visibility = View.INVISIBLE
                    toolbarContent.visibility = View.VISIBLE
                }

            } else {
                expandedContent.visibility = View.VISIBLE
                toolbarContent.visibility = View.VISIBLE

                if (progress <= 0.0) {
                    expandedContent.visibility = View.VISIBLE
                    toolbarContent.visibility = View.INVISIBLE
                }
            }
        })
    }


//    private fun appBarObserver() {
//        val appBarLayout = binding.appbar
//        val expandedContent = binding.layoutExpand
//        val toolbarContent = binding.layoutToolbar
//
//        val defaultColor = ContextCompat.getColor(requireContext(), R.color.blue)
//        val collapseColor = ContextCompat.getColor(requireContext(), R.color.g_white)
//
//        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val totalScrollRange = appBarLayout.totalScrollRange
//
//            if (Math.abs(verticalOffset) >= totalScrollRange) {
//                expandedContent.visibility = View.INVISIBLE
//                toolbarContent.visibility = View.VISIBLE
//
//                val colorAnimation = ObjectAnimator.ofObject(
//                    binding.layoutMain, "backgroundColor", ArgbEvaluator(), defaultColor, collapseColor
//                )
//                colorAnimation.duration = 300 // Geçiş süresi (milisaniye)
//                colorAnimation.start()
//
//            }
//            else {
//                expandedContent.visibility = View.VISIBLE
//                toolbarContent.visibility = View.INVISIBLE
//                val colorAnimation = ObjectAnimator.ofObject(
//                    binding.layoutMain, "backgroundColor", ArgbEvaluator(), defaultColor, defaultColor
//                )
//                colorAnimation.duration = 300 // Geçiş süresi (milisaniye)
//                colorAnimation.start()
//            }
//        })
//
//    }
}