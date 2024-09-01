package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "forecastDay")
data class Forecastday(
    val astro: Astro,
    val date: String,
    val day: Day,
    val hour: List<Hour>
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}
