package com.rurouni.weatherapp.domain

import com.rurouni.weatherapp.data.repository.WeatherRepository
import com.rurouni.weatherapp.data.source.remote.model.ForecastWeather
import com.rurouni.weatherapp.data.source.remote.model.Forecastday
import com.rurouni.weatherapp.data.source.remote.model.HistoryWeather
import com.rurouni.weatherapp.utils.DateOps
import com.rurouni.weatherapp.utils.NetWorkResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeeklyForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    operator fun invoke(): Flow<NetWorkResult<ForecastWeather>> = flow {
        val today = DateOps.getSystemDate(0)
        val yesterday = DateOps.getSystemDate(-1)
        val forecastDayLimit = 3
        val forecastFlow = weatherRepository.getRemoteForecast("48.8567, 2.3508", "en", forecastDayLimit.toString(), null)
        val historicForecastFlow = weatherRepository.getRemoteHistoricForecast("48.8567, 2.3508", "en", yesterday)

        var currentForecast: ForecastWeather? = null
        var historicForecast: HistoryWeather? = null

        forecastFlow.collect { result ->
            if (result is NetWorkResult.Success) {
                currentForecast = result.data
            }
        }

        historicForecastFlow.collect { result ->
            if (result is NetWorkResult.Success) {
                historicForecast = result.data
            }
        }

        val newList = arrayListOf<Forecastday>()

        historicForecast?.let { history ->
            newList.add(history.forecast.forecastday.first())
        }

        currentForecast?.let { forecast ->
            forecast.forecast.forecastday.forEach {
                newList.add(it)
            }

            for (i in (forecastDayLimit)..6) {
                val dt = DateOps.getSystemDate(i)

                val request = weatherRepository.getRemoteForecast("48.8567, 2.3508", "en", "3",dt )

                request.collect { result ->
                    if (result is NetWorkResult.Success) {
                        result.data?.let {
                            newList.add(it.forecast.forecastday.first())
                        }
                    }
                }
            }

            forecast.forecast.forecastday = newList
            emit(NetWorkResult.Success(forecast))
        }
    }
}
