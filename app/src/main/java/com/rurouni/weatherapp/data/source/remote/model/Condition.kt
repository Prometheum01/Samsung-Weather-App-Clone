package com.rurouni.weatherapp.data.source.remote.model

data class Condition(
    val code: Int,
    val icon: String,
    val text: String
)


fun Condition.toIconId() : Int {

    return 0
}