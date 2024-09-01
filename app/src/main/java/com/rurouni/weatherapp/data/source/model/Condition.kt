package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
@Entity(tableName = "condition")

data class Condition(
    val code: Int,
    val text: String
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}
