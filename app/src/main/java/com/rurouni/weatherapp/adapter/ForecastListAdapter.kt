package com.rurouni.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.databinding.ItemWeatherForecastBinding
import com.rurouni.weatherapp.model.WeatherModel
import com.squareup.picasso.Picasso

class ForecastListAdapter(val weatherModel : WeatherModel) : RecyclerView.Adapter<ForecastListAdapter.ForecastListViewHolder>() {

    class  ForecastListViewHolder(val binding : ItemWeatherForecastBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastListViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return weatherModel.forecast?.forecastday?.size ?: 0
    }

    override fun onBindViewHolder(holder: ForecastListViewHolder, position: Int) {
        val binding = holder.binding

        //Current Weather view
        if (position != 0) {
            val newPosition = position - 1

            weatherModel.forecast?. let {
                binding.forecastDate.text = it.forecastday[newPosition].date
                Picasso.get().load("https:${it.forecastday[newPosition].day?.condition?.icon}").into(binding.forecastWeatherIcon);
                binding.forecastTemp.text = "${it.forecastday[newPosition].day?.avgtempC}°C"
                binding.forecastWeatherCondition.text = it.forecastday[newPosition].day?.condition?.text
                binding.forecastItem.visibility = View.VISIBLE
                binding.currentWeatherItem.visibility = View.GONE
            }
        }else {
            binding.locationName.text = "${weatherModel.location?.name}, ${weatherModel.location?.country}"
            binding.lastUpdated.text = "Last updated: ${weatherModel.current?.lastUpdated}"
            Picasso.get().load("https:${weatherModel.current?.condition?.icon}").into(binding.weatherIcon);
            binding.weatherCondition.text = weatherModel.current?.condition?.text
            binding.tempC.text = "${weatherModel.current?.tempC.toString()}°C"

            binding.forecastItem.visibility = View.GONE
            binding.currentWeatherItem.visibility = View.VISIBLE
        }
    }
}