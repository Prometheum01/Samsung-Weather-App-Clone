package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "astro")
data class Astro(
    val sunrise: String,
    val sunset: String
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}