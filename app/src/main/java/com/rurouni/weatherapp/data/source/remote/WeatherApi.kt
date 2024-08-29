package com.rurouni.weatherapp.data.source.remote

import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.data.source.remote.model.HistoryWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getForecast(
        @Query("q") location: String,
        @Query("lang") language: String,
        @Query("days") days: String,
        @Query("dt") date: String?
    ): Response<ForecastWeather>

    @GET("history.json")
    suspend fun getHistoricForecast(@Query("q") location : String, @Query("lang") language: String, @Query("dt") date : String) : Response<HistoryWeather>
}