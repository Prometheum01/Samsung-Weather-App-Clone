package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "forecastWeather")
data class ForecastWeather(
    val current: Current,
    val forecast: Forecast,
    val location: Location
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}