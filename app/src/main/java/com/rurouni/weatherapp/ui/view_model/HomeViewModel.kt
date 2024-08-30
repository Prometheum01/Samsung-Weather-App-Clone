package com.rurouni.weatherapp.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rurouni.weatherapp.data.repository.WeatherRepository
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.data.source.remote.model.Forecastday
import com.rurouni.weatherapp.data.source.remote.model.HistoryWeather
import com.rurouni.weatherapp.domain.WeeklyForecastUseCase
import com.rurouni.weatherapp.ui.model.ForecastDayItem
import com.rurouni.weatherapp.utils.NetWorkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weeklyForecastUseCase: WeeklyForecastUseCase) : ViewModel() {
    private val _currentForecast : MutableLiveData<NetWorkResult<ForecastWeather>> = MutableLiveData()
    val currentForecast : LiveData<NetWorkResult<ForecastWeather>> = _currentForecast

    fun getForecast(location : String) = viewModelScope.launch {
        weeklyForecastUseCase(location, "en", 3).collect { result ->
            _currentForecast.postValue(result)
        }
    }
}