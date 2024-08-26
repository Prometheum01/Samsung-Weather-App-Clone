package com.rurouni.weatherapp.data.source.remote

import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getForecast(@Query("q") location : String, @Query("lang") language: String, @Query("days") days : String) : Response<ForecastWeather>
}