package com.rurouni.weatherapp.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rurouni.weatherapp.data.source.local.db.WeatherDatabase
import com.rurouni.weatherapp.data.source.model.ForecastWeather
import com.rurouni.weatherapp.domain.WeeklyForecastUseCase
import com.rurouni.weatherapp.utils.NetWorkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val weeklyForecastUseCase: WeeklyForecastUseCase, private val weatherDatabase: WeatherDatabase) : ViewModel() {
    private val _currentForecast : MutableLiveData<NetWorkResult<ForecastWeather>> = MutableLiveData()
    val currentForecast : LiveData<NetWorkResult<ForecastWeather>> = _currentForecast

    fun getForecast(location : String) = viewModelScope.launch {
        weeklyForecastUseCase(location, "en", 3).collect { result ->
            _currentForecast.postValue(result)

            result.data?.let {
                weatherDatabase.weatherDao().insert(it)
            }
        }
    }
}