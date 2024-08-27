package com.rurouni.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.R
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
        binding.tvHighTemperature.text = "${list[position].highTemperature}°"
        binding.tvLowTemperature.text = "${list[position].lowTemperature}°"
        binding.tvRainPercentage.text = "%${list[position].rainChance}"

        if (list[position].rainChance <= 25) {
            //No
            binding.imgRainPercentage.setImageResource(R.drawable.ic_no_drop)
        }
        else if(list[position].rainChance <= 75) {
            //Half
            binding.imgRainPercentage.setImageResource(R.drawable.ic_half_drop)
        }
        else {
            //Full
            binding.imgRainPercentage.setImageResource(R.drawable.ic_full_drop)
        }

        val iconCode = list[position].condition.toIntOrNull()

        iconCode?.let {
            val icon = codeToIconId(holder.itemView.context, it)

            icon?.let { id ->
                binding.imgCondition.setImageResource(id)
            }
        }
    }


}