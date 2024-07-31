package com.rurouni.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.databinding.ItemDetailBinding
import com.rurouni.weatherapp.model.WeatherModel
import com.squareup.picasso.Picasso

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailAdapterViewHolder>() {
    private var weatherModel: WeatherModel = WeatherModel()

    fun setModel(newModel: WeatherModel) {
        this.weatherModel = newModel
        notifyDataSetChanged()
    }


    class DetailAdapterViewHolder(val binding: ItemDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailAdapterViewHolder {
        val binding = ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailAdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        var size =  0
        try {
            size = (weatherModel.forecast?.forecastday?.first()?.hour?.size)?: 0
            if (size != 0) {
                size++
            }
        }catch (e : Exception) {

        }
        return size
    }

    override fun onBindViewHolder(holder: DetailAdapterViewHolder, position: Int) {
        val binding = holder.binding

        try {
            val hours = weatherModel.forecast?.forecastday?.first()?.hour

            val day = weatherModel.forecast?.forecastday?.first()?.day

            if (position != 0) {
                val newPosition = position - 1

                hours?.let {
                    binding.detailHourTime.text = it[newPosition].time
                    Picasso.get().load("https:${it[newPosition].condition?.icon}")
                        .into(binding.detailHourWeatherIcon);
                    binding.detailHourTemp.text = "${it[newPosition].tempC}°C"
                    binding.detailHourWeatherCondition.text = it[newPosition].condition?.text
                    binding.detailHourItem.visibility = View.VISIBLE
                    binding.detailMainItem.visibility = View.GONE
                }
            } else {
                binding.detailMainLocationName.text =
                    "${weatherModel.location?.name}, ${weatherModel.location?.country}"
                binding.detailMainLastUpdated.text =
                    "Last updated: ${weatherModel.current?.lastUpdated}"
                Picasso.get().load("https:${day?.condition?.icon}")
                    .into(binding.detailMainWeatherIcon);
                binding.detailMainWeatherCondition.text = day?.condition?.text
                binding.detailMainTempC.text = "${day?.avgtempC.toString()}°C"

                binding.detailHourItem.visibility = View.GONE
                binding.detailMainItem.visibility = View.VISIBLE
            }

        } catch (e: Exception) {

        }


    }
}
