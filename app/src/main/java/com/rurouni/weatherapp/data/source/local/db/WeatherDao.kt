package com.rurouni.weatherapp.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rurouni.weatherapp.data.source.model.ForecastWeather

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forecastWeather: ForecastWeather)

    @Query("SELECT * FROM forecastWeather")
    suspend fun getAll(): List<ForecastWeather>
}