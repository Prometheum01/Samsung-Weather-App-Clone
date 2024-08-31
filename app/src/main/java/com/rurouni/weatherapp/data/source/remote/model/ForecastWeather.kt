package com.rurouni.weatherapp.data.source.remote.model

import java.io.Serializable

data class ForecastWeather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
) : Serializable