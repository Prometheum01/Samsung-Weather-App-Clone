package com.rurouni.weatherapp.data.source.remote.model

import java.io.Serializable

data class Forecast(
    var forecastday: List<Forecastday>
) : Serializable

