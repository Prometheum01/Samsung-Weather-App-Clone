package com.rurouni.weatherapp.ui.adapter

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

object AdapterAnimation {
    val duration = 500L

    fun animateTextColorChange(textView: TextView, fromColor: Int, toColor: Int) : ObjectAnimator {
        val colorAnimation = ObjectAnimator.ofArgb(textView, "textColor", fromColor, toColor)
        colorAnimation.duration = duration
        colorAnimation.start()
        return colorAnimation
    }

    fun animateImageViewTintColorChange(imageView: ImageView, fromColor: Int, toColor: Int) : ValueAnimator {
        val colorAnimation = ValueAnimator.ofArgb(fromColor, toColor)
        colorAnimation.duration = duration
        colorAnimation.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            imageView.setColorFilter(animatedValue)
        }
        colorAnimation.start()
        return colorAnimation
    }

    fun animateCardViewBackgroundColor(cardView: CardView, startColor: Int, endColor: Int) {
        val colorAnimator = ValueAnimator.ofArgb(startColor, endColor)
        colorAnimator.duration = duration
        colorAnimator.interpolator = AccelerateDecelerateInterpolator()
        colorAnimator.addUpdateListener { animator ->
            cardView.setCardBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimator.start()
    }

    fun animateBackgroundColor(view: View, startColor: Int, endColor: Int) {
        val colorAnimator = ValueAnimator.ofArgb(startColor, endColor)
        colorAnimator.duration = duration
        colorAnimator.interpolator = AccelerateDecelerateInterpolator()
        colorAnimator.addUpdateListener { animator ->
            view.setBackgroundColor(animator.animatedValue as Int)
        }
        colorAnimator.start()
    }
}