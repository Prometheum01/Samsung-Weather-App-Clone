package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "day")
data class Day(
    val avghumidity: Int,
    val avgtemp_c: Double,
    val condition: Condition,
    val daily_chance_of_rain: Int,
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val uv: Double
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}