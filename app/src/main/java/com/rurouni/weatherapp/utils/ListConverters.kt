package com.rurouni.weatherapp.utils

import com.rurouni.weatherapp.data.source.model.Forecast
import com.rurouni.weatherapp.data.source.model.Forecastday
import com.rurouni.weatherapp.data.source.model.Location
import com.rurouni.weatherapp.data.source.model.getTime
import com.rurouni.weatherapp.ui.model.ForecastDayItem
import com.rurouni.weatherapp.ui.model.HourlyItem

//This class transform api data to recycler view item data

object ListConverters {

    private fun indexToDayLabel(index : Int, forecastDay: Forecastday) : String {
        return when (index) {
            0 -> {
                "Yesterday"
            }
            1 -> {
                "Today"
            }
            2 -> {
                "Tomorrow"
            }
            else -> {
                DateOps.dateToWeeklyDay(forecastDay.date)
            }
        }
    }

    fun forecastToDayItem(forecast: Forecast): List<ForecastDayItem> {
        val list = ArrayList<ForecastDayItem>()

        forecast.forecastday.forEachIndexed { index, forecastday ->
            val item = ForecastDayItem(indexToDayLabel(index, forecastday), forecastday.day.condition.code.toString(), forecastday.day.maxtemp_c.toString(), forecastday.day.mintemp_c.toString(), forecastday.day.daily_chance_of_rain)
            list.add(item)
        }

        return list
    }

    private fun findStartingHourIndex(location: Location): Int {
        val time = location.getTime().split(":")

        val hour = time[0].toIntOrNull()

        hour?.let {
            return hour
        }

        return -1
    }

    fun forecastToHourlyItem(location: Location, forecast: Forecast): List<HourlyItem> {
        var startingIndex = findStartingHourIndex(location)
        val list = ArrayList<HourlyItem>()

        var counter = 24
        var dayIndex = 0

        while (counter > 0) {
            val hours = forecast.forecastday[dayIndex].hour

            hours.forEachIndexed { index, hour ->
                if (index >= startingIndex && counter > 0) {
                    list.add(
                        HourlyItem(
                            hour.getTime(),
                            hour.condition.code.toString(),
                            hour.temp_c.toFloat(),
                            hour.chance_of_rain
                        )
                    )
                    counter--
                }
            }
            startingIndex = 0
            dayIndex++
        }

        return list
    }
}