package com.rurouni.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateOps {

    fun getSystemDate(count : Int = 0) : String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, count)

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "${year}-${month}-${day}"
    }

    fun dateToWeeklyDay(date : String) : String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val parsedDate = formatter.parse(date)

        val calendar = Calendar.getInstance()
        calendar.time = parsedDate

        val dayOfWeek = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)

        return dayOfWeek
    }

}