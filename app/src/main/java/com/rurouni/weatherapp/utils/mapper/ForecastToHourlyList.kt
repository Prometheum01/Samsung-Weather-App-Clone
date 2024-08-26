package com.rurouni.weatherapp.utils.mapper

import com.rurouni.weatherapp.data.source.remote.model.Forecast
import com.rurouni.weatherapp.data.source.remote.model.Location
import com.rurouni.weatherapp.data.source.remote.model.getTime
import com.rurouni.weatherapp.ui.model.HourlyItem

object ForecastToHourlyList {
    private fun findStartingHourIndex(location: Location): Int {
        val time = location.getTime().split(":")

        val hour = time[0].toIntOrNull()

        hour?.let {
            return hour
        }

        return -1
    }


    operator fun invoke(location: Location, forecast: Forecast): List<HourlyItem> {
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
                            hour.temp_c.toString()
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
