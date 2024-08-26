package com.rurouni.weatherapp.data.source.remote.model

data class ForecastWeather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)