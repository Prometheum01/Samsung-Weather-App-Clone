package com.rurouni.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.rurouni.weatherapp.databinding.HourlyItemBinding
import com.rurouni.weatherapp.ui.adapter.AdapterAnimation.animateImageViewTintColorChange
import com.rurouni.weatherapp.ui.adapter.AdapterAnimation.animateTextColorChange
import com.rurouni.weatherapp.ui.model.ColorState
import com.rurouni.weatherapp.ui.model.HourlyItem
import com.rurouni.weatherapp.utils.Utils.codeToIconId

class HourlyListAdapter(private val recyclerView: RecyclerView) : RecyclerView.Adapter<HourlyListAdapter.ViewHolder>(){

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

        if (isAnimate) {
            animateTextColorChange(binding.tvHour, colorState.currentPalette.onPrimary, colorState.nextPalette.onPrimary).doOnEnd {
            }
            animateTextColorChange(binding.tvTemperature, colorState.currentPalette.onPrimary, colorState.nextPalette.onPrimary).doOnEnd {
            }
            animateImageViewTintColorChange(binding.imgCondition, colorState.currentPalette.onPrimary, colorState.nextPalette.onPrimary).doOnEnd {
                isAnimate = false
                setColorState(nextColorState)
            }
        }

        binding.tvHour.text = list[position].hour
        binding.tvTemperature.text = "${list[position].temperature}°"

        val iconCode = list[position].condition.toIntOrNull()
        iconCode?.let {
            val icon = codeToIconId(holder.itemView.context, it)
            icon?.let { id ->
                binding.imgCondition.setImageResource(id)
            }
        }

        if (!isAnimate) {
            binding.tvHour.setTextColor(colorState.currentPalette.onPrimary)
            binding.tvTemperature.setTextColor(colorState.currentPalette.onPrimary)
            binding.imgCondition.setColorFilter(colorState.currentPalette.onPrimary)
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
