package com.rurouni.weatherapp.service

import com.rurouni.weatherapp.model.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query



interface WeatherApi {
    @GET("forecast.json")
    suspend fun getForecast(@Query("key") key : String, @Query("q") location : String, @Query("days") days : String) : Response<WeatherModel>
}