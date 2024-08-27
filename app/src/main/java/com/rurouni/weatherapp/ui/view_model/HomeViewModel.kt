package com.rurouni.weatherapp.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rurouni.weatherapp.data.repository.WeatherRepository
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.utils.NetWorkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _response : MutableLiveData<NetWorkResult<ForecastWeather>> = MutableLiveData()
    val response : LiveData<NetWorkResult<ForecastWeather>> = _response

    fun getForecast() = viewModelScope.launch {

        weatherRepository.getRemoteForecast("48.8567,2.3508", "en", "3").collect {values ->

            _response.value = values

        }

    }


}