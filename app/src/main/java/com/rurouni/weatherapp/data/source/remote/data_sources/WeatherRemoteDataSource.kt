package com.rurouni.weatherapp.data.source.remote.data_sources

import com.rurouni.weatherapp.data.source.remote.WeatherApi
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(private val weatherApi: WeatherApi) {
    suspend fun getForecast(location : String, language: String, days : String, date : String?) = weatherApi.getForecast(location, language, days, date)

    suspend fun getHistoricForecast(location: String, language: String, date: String) = weatherApi.getHistoricForecast(location, language, date)
}