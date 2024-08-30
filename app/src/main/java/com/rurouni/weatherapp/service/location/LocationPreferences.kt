package com.rurouni.weatherapp.service.location

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationPreferences @Inject constructor (@ApplicationContext private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("LocationPrefs", Context.MODE_PRIVATE)

    fun saveLocation(location: Location) {
        with(sharedPreferences.edit()) {
            putFloat("latitude", location.latitude.toFloat())
            putFloat("longitude", location.longitude.toFloat())
            apply()
        }
    }

    fun getSavedLocation(): Location? {
        val latitude = sharedPreferences.getFloat("latitude", Float.NaN)
        val longitude = sharedPreferences.getFloat("longitude", Float.NaN)

        return if (latitude.isNaN() || longitude.isNaN()) {
            null
        } else {
            Location("").apply {
                this.latitude = latitude.toDouble()
                this.longitude = longitude.toDouble()
            }
        }
    }
}

