package com.rurouni.weatherapp.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rurouni.weatherapp.R
import java.io.Serializable

@Entity(tableName = "current")
data class Current(
    val condition: Condition,
    val feelslike_c: Double,
    val humidity: Int,
    val temp_c: Double,
    val is_day: Int,
    val uv: Double,
    val wind_kph: Double,
) : Serializable {
    @PrimaryKey
    var id: Int = 1
}


fun Current.toLottie() : Int {
    return when (condition.code) {
        // Güneşli ve açık hava
        1000 -> if(is_day == 1) R.raw.lottie_sun else R.raw.lottie_night

        // Bulutlu ve parçalı bulutlu hava
        1003, 1006, 1009 -> if(is_day == 1) R.raw.lottie_cloud_sun else R.raw.lottie_cloud_night

        // Sisli ve puslu hava
        1030, 1135, 1147 -> if(is_day == 1) R.raw.lottie_fog_sun else R.raw.lottie_fog

        // Hafif yağmur ve olasılık
        1063, 1150, 1153, 1180, 1183 -> if(is_day == 1) R.raw.lottie_rain_sun else R.raw.lottie_rain_night

        // Orta şiddetli yağmur
        1186, 1189, 1192 -> if(is_day == 1) R.raw.lottie_rain_sun else R.raw.lottie_rain_night

        // Şiddetli yağmur
        1195, 1246 -> if(is_day == 1) R.raw.lottie_rain_sun else R.raw.lottie_rain_night

        // Hafif kar ve olasılık
        1066, 1210, 1213, 1255 -> if(is_day == 1) R.raw.lottie_snow_sun else R.raw.lottie_snow_night

        // Orta ve şiddetli kar
        1114, 1117, 1216, 1219, 1222, 1225, 1258 -> if(is_day == 1) R.raw.lottie_snow_sun else R.raw.lottie_snow_night

        // Dolu ve buz taneleri
        1237, 1261, 1264 -> if(is_day == 1) R.raw.lottie_snow_sun else R.raw.lottie_snow_night

        // Fırtınalı hava
        1087, 1273, 1276, 1279, 1282 -> R.raw.lottie_thunder_cloud

        // Dondurucu yağmur ve çiseleme
        1069, 1072, 1168, 1171, 1198, 1201 -> R.raw.lottie_snow_cloud

        // Hafif kar ve karla karışık yağmur
        1069, 1072, 1204, 1207, 1249, 1252 -> if(is_day == 1) R.raw.lottie_rain_sun else R.raw.lottie_rain_night

        // Genel varsayılan animasyon
        else -> R.raw.lottie_sun
    }
}