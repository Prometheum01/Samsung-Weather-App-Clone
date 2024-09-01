package com.rurouni.weatherapp.data.source.local.db

import androidx.room.TypeConverter
import com.rurouni.weatherapp.data.source.model.Condition
import com.rurouni.weatherapp.data.source.model.Current
import com.rurouni.weatherapp.data.source.model.ForecastWeather
import com.rurouni.weatherapp.data.source.model.Location

import com.google.gson.Gson
import com.rurouni.weatherapp.data.source.model.Astro
import com.rurouni.weatherapp.data.source.model.Day
import com.rurouni.weatherapp.data.source.model.Forecast
import com.rurouni.weatherapp.data.source.model.Hour

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromForecastWeather(forecastWeather: ForecastWeather): String {
        return gson.toJson(forecastWeather)
    }

    @TypeConverter
    fun toForecastWeather(data: String): ForecastWeather {
        return gson.fromJson(data, ForecastWeather::class.java)
    }

    @TypeConverter
    fun fromCurrent(current: Current): String {
        return gson.toJson(current)
    }

    @TypeConverter
    fun toCurrent(data: String): Current {
        return gson.fromJson(data, Current::class.java)
    }

    @TypeConverter
    fun fromCondition(condition: Condition): String {
        return gson.toJson(condition)
    }

    @TypeConverter
    fun toCondition(data: String): Condition {
        return gson.fromJson(data, Condition::class.java)
    }

    @TypeConverter
    fun fromForecast(forecast: Forecast): String {
        return gson.toJson(forecast)
    }

    @TypeConverter
    fun toForecast(data: String): Forecast {
        return gson.fromJson(data, Forecast::class.java)
    }

    @TypeConverter
    fun fromAstro(astro: Astro): String {
        return gson.toJson(astro)
    }

    @TypeConverter
    fun toAstro(data: String): Astro {
        return gson.fromJson(data, Astro::class.java)
    }

    @TypeConverter
    fun fromDay(day: Day): String {
        return gson.toJson(day)
    }

    @TypeConverter
    fun toDay(data: String): Day {
        return gson.fromJson(data, Day::class.java)
    }

    @TypeConverter
    fun fromHour(hour: Hour): String {
        return gson.toJson(hour)
    }

    @TypeConverter
    fun toHour(data: String): Hour {
        return gson.fromJson(data, Hour::class.java)
    }




    @TypeConverter
    fun fromLocation(location: Location): String {
        return gson.toJson(location)
    }

    @TypeConverter
    fun toLocation(data: String): Location {
        return gson.fromJson(data, Location::class.java)
    }


}
