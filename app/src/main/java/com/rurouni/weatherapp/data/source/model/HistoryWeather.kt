package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "historyWeather")
data class HistoryWeather(
    val location: Location,
    val forecast: Forecast
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}