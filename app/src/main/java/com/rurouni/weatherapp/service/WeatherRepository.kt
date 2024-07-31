package com.rurouni.weatherapp.service

import com.rurouni.weatherapp.model.WeatherModel
import com.rurouni.weatherapp.view.DAYS
import com.rurouni.weatherapp.view.KEY
import retrofit2.Response

class WeatherRepository(private val weatherApi: WeatherApi) {
    suspend fun getWeather(location : String) : Response<WeatherModel> {
        return weatherApi.getWeather(KEY, location, DAYS )
    }

    suspend fun getWeatherWithDate(location : String, date : String) : Response<WeatherModel> {
        return weatherApi.getWeatherWithDate(KEY, location, date)
    }
}