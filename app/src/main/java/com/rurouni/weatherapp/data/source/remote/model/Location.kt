package com.rurouni.weatherapp.data.source.remote.model

data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    val localtime_epoch: Int,
    val lon: Double,
    val name: String,
    val region: String,
    val tz_id: String
)


fun Location.getTime() : String {
    val currentDate = localtime

    val time = currentDate.split(" ").last()

    return time
}