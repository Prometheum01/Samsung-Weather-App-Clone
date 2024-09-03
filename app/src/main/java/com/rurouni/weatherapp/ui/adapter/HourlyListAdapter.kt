package com.rurouni.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.R
import com.rurouni.weatherapp.databinding.HourlyItemBinding
import com.rurouni.weatherapp.ui.animation.CustomAnimation
import com.rurouni.weatherapp.ui.animation.CustomAnimation.animateImageViewTintColorChange
import com.rurouni.weatherapp.ui.animation.CustomAnimation.animateTextColorChange
import com.rurouni.weatherapp.ui.model.ColorState
import com.rurouni.weatherapp.ui.model.HourlyItem
import com.rurouni.weatherapp.ui.model.TemperatureGraphData
import com.rurouni.weatherapp.utils.Utils.codeToIconId

class HourlyListAdapter() : RecyclerView.Adapter<HourlyListAdapter.ViewHolder>(){

    private var list = listOf<HourlyItem>()

    fun setList(list: List<HourlyItem>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: HourlyItemBinding) : RecyclerView.ViewHolder(binding.root) {
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

        //This function starts the animation of the list elements if they are in animation mode.
        if (isAnimate) {
            animateTextColorChange(binding.tvHour, colorState.currentPalette.onPrimary, colorState.nextPalette.onPrimary)
            animateTextColorChange(binding.tvTemperature, colorState.currentPalette.onPrimary, colorState.nextPalette.onPrimary)
            animateTextColorChange(binding.tvRainPercentage, colorState.currentPalette.onSecondary, colorState.nextPalette.onSecondary)
            animateImageViewTintColorChange(binding.imgRainPercentage, colorState.currentPalette.dropColor, colorState.nextPalette.dropColor)
            binding.graphTemperature.animateCircleColorChange(colorState.currentPalette.dropColor, colorState.nextPalette.dropColor, CustomAnimation.duration).doOnEnd {
                isAnimate = false
                setColorState(nextColorState)
            }
        }

        binding.tvHour.text = list[position].hour
        binding.tvTemperature.text = "${list[position].temperature}°"
        binding.tvRainPercentage.text = "%${list[position].rainPercentage}"

        val min = list.minOfOrNull { it.temperature }
        val max = list.maxOfOrNull { it.temperature }

        //This condition creates temperature graph except first one's left side and last one's right side
        if (position == 0) {
            val firstData = TemperatureGraphData(min!!, max!!, 0f, list[position].temperature, list[position + 1].temperature)
            holder.binding.graphTemperature.setValues(firstData)
        }
        else if (position == list.size - 1) {
            val lastData = TemperatureGraphData(min!!, max!!, list[position - 1].temperature, list[position].temperature, 0f)
            holder.binding.graphTemperature.setValues(lastData)
        }
        else {
            val otherData = TemperatureGraphData(min!!, max!!, list[position - 1].temperature, list[position].temperature, list[position + 1].temperature)
            holder.binding.graphTemperature.setValues(otherData)
        }

        if (list[position].rainPercentage <= 25) {
            //No
            binding.imgRainPercentage.setImageResource(R.drawable.ic_no_drop)
        }
        else if(list[position].rainPercentage <= 75) {
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

        //This function sets color of the list elements if they are not in animation mode.
        if (!isAnimate) {
            binding.tvHour.setTextColor(colorState.currentPalette.onPrimary)
            binding.tvTemperature.setTextColor(colorState.currentPalette.onPrimary)
            binding.tvRainPercentage.setTextColor(colorState.currentPalette.onSecondary)
            binding.imgRainPercentage.setColorFilter(colorState.currentPalette.dropColor)
            binding.graphTemperature.setCircleColor(colorState.currentPalette.dropColor)
        }
    }

    private lateinit var colorState: ColorState
    private lateinit var nextColorState: ColorState
    private var isAnimate = false

    fun setColorState(state: ColorState) {
        this.colorState = state
        notifyDataSetChanged()
    }

    fun animate(nextColorState: ColorState) {
        this.isAnimate = true
        this.nextColorState = nextColorState
        notifyDataSetChanged()
    }
}
