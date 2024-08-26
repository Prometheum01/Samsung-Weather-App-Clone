package com.rurouni.weatherapp.utils.mapper

import com.rurouni.weatherapp.data.source.remote.model.Forecast
import com.rurouni.weatherapp.data.source.remote.model.Forecastday
import com.rurouni.weatherapp.ui.model.ForecastDayItem

class ForecastToDayItemList {
    private fun indexToDayLabel(index : Int, forecastDay: Forecastday) : String {
        when (index) {
            0 -> {
                return "Today"
            }
            1 -> {
                return "Tomorrow"
            }
            else -> {
                return ApiDateToDay(forecastDay.date)
            }
        }
    }

    operator fun invoke(forecast: Forecast): List<ForecastDayItem> {
        val list = ArrayList<ForecastDayItem>()

        forecast.forecastday.forEachIndexed { index, forecastday ->
            val item = ForecastDayItem(indexToDayLabel(index, forecastday), forecastday.day.condition.code.toString(), forecastday.day.maxtemp_c.toString(), forecastday.day.mintemp_c.toString())
            list.add(item)
            list.add(item)
            list.add(item)
        }

        return list
    }
}
