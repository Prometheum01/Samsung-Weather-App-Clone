package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "current")
data class Current(
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val temp_c: Double,
    val uv: Double,
    val wind_kph: Double,
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}