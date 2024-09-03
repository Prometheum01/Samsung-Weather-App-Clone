package com.rurouni.weatherapp.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rurouni.weatherapp.data.source.model.ForecastWeather
import com.rurouni.weatherapp.databinding.FragmentLoadingBinding
import com.rurouni.weatherapp.ui.view.components.Messages
import com.rurouni.weatherapp.ui.view_model.HomeViewModel
import com.rurouni.weatherapp.ui.view_model.LoadingViewModel
import com.rurouni.weatherapp.utils.ApiResultHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingFragment : Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val loadingViewModel : LoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeSavedData()
        observeForecastData()
        loadingViewModel.checkHasSavedData()
    }

    //This  function observe loading view model saved data and navigate home with this data
    private fun observeSavedData() {
        loadingViewModel.savedData.observe(viewLifecycleOwner) {
            navigateHome(it)
        }
    }

    //This  function navigate home fragment with forecastWeather data
    private fun navigateHome(forecastWeather: ForecastWeather) {
        if (findNavController().currentDestination?.label != "fragment_home") {
            val action = LoadingFragmentDirections.actionLoadingFragmentToHomeFragment(forecastWeather)
            findNavController().navigate(action)
        }
    }

    //This  function observe forecast data from home view model and navigate home with this data
    private fun observeForecastData() {
        try {
            homeViewModel.currentForecast.observe(requireActivity()) { response ->
                val apiResultHandler = ApiResultHandler<ForecastWeather>(requireContext(),
                    onLoading = {

                    },
                    onSuccess = { data ->
                        navigateHome(data!!)
                    },
                    onFailure = {
                        Messages.noInternetMessage(requireContext())
                    })
                apiResultHandler.handleApiResult(response)
            }
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}