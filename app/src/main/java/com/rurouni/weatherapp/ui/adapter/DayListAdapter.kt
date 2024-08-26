package com.rurouni.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.databinding.DayItemBinding
import com.rurouni.weatherapp.ui.model.ForecastDayItem
import com.rurouni.weatherapp.utils.Utils.codeToIconId

class DayListAdapter : RecyclerView.Adapter<DayListAdapter.ViewHolder>() {

    private var list = listOf<ForecastDayItem>()

    fun setList(list: List<ForecastDayItem>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: DayItemBinding) : RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.tvDay.text = list[position].day
        binding.tvHighTemperature.text = "${list[position].highTemperature}°C"
        binding.tvLowTemperature.text = "${list[position].lowTemperature}°C"
        val iconCode = list[position].condition.toIntOrNull()

        iconCode?.let {
            val icon = codeToIconId(holder.itemView.context, it)

            icon?.let { id ->
                binding.imgCondition.setImageResource(id)
            }
        }
    }


}