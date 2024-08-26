package com.rurouni.weatherapp.utils.mapper


import android.annotation.SuppressLint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object ApiDateToDay {
    @SuppressLint("NewApi")
    operator fun invoke(date: String): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = LocalDate.parse(date, formatter)

        // TODO: Dil seçeneği ekle
        val dayOfWeek = formattedDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("en"))

        return dayOfWeek
    }
}