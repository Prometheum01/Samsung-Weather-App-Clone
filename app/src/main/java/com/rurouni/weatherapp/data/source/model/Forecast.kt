package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "forecast")
data class Forecast(
    var forecastday: List<Forecastday>
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}

