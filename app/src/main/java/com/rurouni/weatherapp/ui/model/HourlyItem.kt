package com.rurouni.weatherapp.ui.model

data class HourlyItem(
    val hour: String,
    val condition: String,
    val temperature: Float,
    val rainPercentage : Int
)


