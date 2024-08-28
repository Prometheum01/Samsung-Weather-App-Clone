package com.rurouni.weatherapp.ui.model

import android.content.Context
import com.rurouni.weatherapp.R
import com.rurouni.weatherapp.utils.Utils

data class ColorState(
    val currentPalette: ColorPalette,
    val nextPalette: ColorPalette,
    val theme: ColorTheme,
)

enum class ColorTheme() {
    MAIN,
    SYSTEM
}

data class ColorPalette(
    val primary: Int,
    val onPrimary: Int,
    val secondary: Int,
    val onSecondary: Int
){
    companion object {

        fun mainPalette(context: Context) : ColorPalette {
            val primary = Utils.getColor(context, R.color.primaryMain)
            val onPrimary = Utils.getColor(context, R.color.onPrimaryMain)
            val secondary = Utils.getColor(context, R.color.secondaryMain)
            val onSecondary = Utils.getColor(context, R.color.onSecondaryMain)

            return ColorPalette(primary, onPrimary, secondary, onSecondary)
        }

        fun systemPalette(context: Context) : ColorPalette {
            val primary = Utils.getColor(context, R.color.primarySystem)
            val onPrimary = Utils.getColor(context, R.color.onPrimarySystem)
            val secondary = Utils.getColor(context, R.color.secondarySystem)
            val onSecondary = Utils.getColor(context, R.color.onSecondarySystem)

            return ColorPalette(primary, onPrimary, secondary, onSecondary)
        }
    }
}
