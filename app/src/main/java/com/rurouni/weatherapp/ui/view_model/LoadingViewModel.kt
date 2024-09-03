package com.rurouni.weatherapp.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rurouni.weatherapp.data.repository.WeatherRepository
import com.rurouni.weatherapp.data.source.model.ForecastWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoadingViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {
    private val _savedData : MutableLiveData<ForecastWeather> = MutableLiveData()
    val savedData : LiveData<ForecastWeather> = _savedData

    fun checkHasSavedData() {
        viewModelScope.launch {
            val response = weatherRepository.getLocalForecast()
            response?.let {
                _savedData.value = it
            }

        }
    }

}