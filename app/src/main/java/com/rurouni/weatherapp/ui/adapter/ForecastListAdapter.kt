package com.rurouni.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.databinding.ItemWeatherForecastBinding
import com.rurouni.weatherapp.model.WeatherModel
import com.rurouni.weatherapp.ui.view.fragments.MainPageViewDirections
import com.squareup.picasso.Picasso

class ForecastListAdapter() : RecyclerView.Adapter<ForecastListAdapter.ForecastListViewHolder>() {

    private  var weatherModel : WeatherModel = WeatherModel()

    fun setModel(newModel : WeatherModel) {
        this.weatherModel = newModel
        notifyDataSetChanged()
    }

    class  ForecastListViewHolder(val binding : ItemWeatherForecastBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastListViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        var size =  0
        try {
            size = (weatherModel.forecast?.forecastday?.size)?: 0
            if (size != 0) {
                size++
            }
        }catch (e : Exception) {

        }
        return size
    }

    override fun onBindViewHolder(holder: ForecastListViewHolder, position: Int) {
        val binding = holder.binding

        if (position != 0) {
            val newPosition = position - 1

            weatherModel.forecast?. let {
                binding.forecastDate.text = it.forecastday[newPosition].date
                Picasso.get().load("https:${it.forecastday[newPosition].day?.condition?.icon}").into(binding.forecastWeatherIcon);
                binding.forecastTemp.text = "${it.forecastday[newPosition].day?.avgtempC}°C"
                binding.forecastWeatherCondition.text = it.forecastday[newPosition].day?.condition?.text
                binding.forecastItem.visibility = View.VISIBLE
                binding.currentWeatherItem.visibility = View.GONE

                binding.forecastItem.setOnClickListener { view ->
                    val action = MainPageViewDirections.actionMainPageViewToDetailView(it.forecastday[newPosition].date.toString(),"${weatherModel.location?.lat},${weatherModel.location?.lon}")
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }
        else {
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