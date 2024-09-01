package com.rurouni.weatherapp.data.repository

import android.content.Context
import com.rurouni.weatherapp.data.source.local.data_sources.WeatherLocalDataSource
import com.rurouni.weatherapp.data.source.remote.data_sources.WeatherRemoteDataSource
import com.rurouni.weatherapp.data.source.model.ForecastWeather
import com.rurouni.weatherapp.data.source.model.HistoryWeather
import com.rurouni.weatherapp.data.source.remote.toResultFlow
import com.rurouni.weatherapp.utils.NetWorkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor (@ApplicationContext private val context: Context, private val remoteDataSource: WeatherRemoteDataSource, private val localDataSource: WeatherLocalDataSource) {
    suspend fun getRemoteForecast(location : String, language: String, days : String, date : String?) : Flow<NetWorkResult<ForecastWeather>> {
        return toResultFlow(context){
            remoteDataSource.getForecast(location, language, days, date)
        }
    }

    suspend fun getRemoteHistoricForecast(location: String, language: String, date : String) : Flow<NetWorkResult<HistoryWeather>> {
        return toResultFlow(context) {
            remoteDataSource.getHistoricForecast(location, language, date)
        }
    }

    suspend fun saveLocalForecast(forecastWeather: ForecastWeather) {
        localDataSource.saveWeather(forecastWeather)
    }

    suspend fun getLocalForecast() : ForecastWeather? {
        return localDataSource.getWeather()
    }
}