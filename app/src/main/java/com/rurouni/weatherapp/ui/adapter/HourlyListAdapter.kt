package com.rurouni.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.R
import com.rurouni.weatherapp.databinding.HourlyItemBinding
import com.rurouni.weatherapp.ui.model.HourlyItem
import com.rurouni.weatherapp.utils.Utils
import com.rurouni.weatherapp.utils.Utils.codeToIconId

class HourlyListAdapter : RecyclerView.Adapter<HourlyListAdapter.ViewHolder>() {

    private var list = listOf<HourlyItem>()

    fun setList(list: List<HourlyItem>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding : HourlyItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HourlyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding

        binding.tvHour.text = list[position].hour
        val iconCode = list[position].condition.toIntOrNull()

        iconCode?.let {
            val icon = codeToIconId(holder.itemView.context, it)

            icon?.let { id ->
                binding.imgCondition.setImageResource(id)
            }
        }

        binding.tvTemperature.text = "${list[position].temperature}°C"
    }


}