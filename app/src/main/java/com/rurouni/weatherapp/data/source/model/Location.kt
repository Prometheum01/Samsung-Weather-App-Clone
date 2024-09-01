package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "location")
data class Location(
    val lat: Double,
    val localtime: String,
    val lon: Double,
    val name: String,
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}


fun Location.getTime() : String {
    val currentDate = localtime

    val time = currentDate.split(" ").last()

    return time
}