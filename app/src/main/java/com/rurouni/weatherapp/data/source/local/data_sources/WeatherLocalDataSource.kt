package com.rurouni.weatherapp.data.source.local.data_sources

import com.rurouni.weatherapp.data.source.local.db.WeatherDao
import com.rurouni.weatherapp.data.source.model.ForecastWeather
import javax.inject.Inject

class WeatherLocalDataSource @Inject constructor(private val weatherDao: WeatherDao) {
    suspend fun saveWeather(forecastWeather: ForecastWeather) {
        weatherDao.insert(forecastWeather)
    }

    suspend fun getWeather() : ForecastWeather? {
        val list = weatherDao.getAll()

        if (list.isNotEmpty()) {
            return list.last()
        }
        return null
    }
}