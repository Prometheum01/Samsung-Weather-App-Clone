package com.rurouni.weatherapp.ui.model

data class ForecastDayItem(
    val day: String,
    val condition: String,
    val highTemperature: String,
    val lowTemperature: String,
    val rainChance : Int,
)
