package com.rurouni.weatherapp.data.repository

import android.content.Context
import com.rurouni.weatherapp.data.source.remote.data_sources.WeatherRemoteDataSource
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.data.source.remote.toResultFlow
import com.rurouni.weatherapp.utils.NetWorkResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor (@ApplicationContext private val context: Context, private val remoteDataSource: WeatherRemoteDataSource) {
    suspend fun getRemoteForecast(location : String, language: String, days : String) : Flow<NetWorkResult<ForecastWeather>> {
        return toResultFlow(context){
            remoteDataSource.getForecast(location, language, days)
        }
    }
}