package com.rurouni.weatherapp.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.databinding.FragmentLoadingBinding
import com.rurouni.weatherapp.ui.view.components.Messages
import com.rurouni.weatherapp.ui.view_model.HomeViewModel
import com.rurouni.weatherapp.utils.ApiResultHandler

class LoadingFragment : Fragment() {
    //Binding
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkHasCachedData()
    }

    private fun checkHasCachedData() {
        val hasData = false

        if (hasData) {
            //TODO:Navigate Home Fragment
        }else {
            observeForecastData()
        }
    }

    private fun observeForecastData() {
        try {
            homeViewModel.currentForecast.observe(requireActivity()) { response ->
                val apiResultHandler = ApiResultHandler<ForecastWeather>(requireContext(),
                    onLoading = {

                    },
                    onSuccess = { data ->
                        //TODO:Save Room
                        //TODO:Navigate Home
                        if (findNavController().currentDestination?.label != "fragment_home") {
                            val action = LoadingFragmentDirections.actionLoadingFragmentToHomeFragment(data!!)
                            findNavController().navigate(action)
                        }
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