package com.rurouni.weatherapp.service

import com.rurouni.weatherapp.model.WeatherModel
import com.rurouni.weatherapp.view.BASE_URL
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("forecast.json")
    suspend fun getWeather(@Query("key") key : String, @Query("q") location : String, @Query("days") days : String) : Response<WeatherModel>

    @GET("forecast.json")
    suspend fun getWeatherWithDate(@Query("key") key : String, @Query("q") location : String, @Query("dt") date : String) : Response<WeatherModel>

    companion object {
        var retrofitService: WeatherApi? = null
        fun getInstance() : WeatherApi {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(WeatherApi::class.java)
            }
            return retrofitService!!
        }

    }
}