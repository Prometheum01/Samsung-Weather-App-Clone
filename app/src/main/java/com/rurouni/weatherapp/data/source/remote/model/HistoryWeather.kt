package com.rurouni.weatherapp.data.source.remote.model

import java.io.Serializable

data class HistoryWeather(
    val location: Location,
    val forecast: Forecast
) : Serializable