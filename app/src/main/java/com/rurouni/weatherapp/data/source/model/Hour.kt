package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "hour")
data class Hour(
    val chance_of_rain: Int,
    val condition: Condition,
    val temp_c: Double,
    val time: String,
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}


fun Hour.getTime() : String {
    val currentDate = time

    val time = currentDate.split(" ").last()

    return time
}