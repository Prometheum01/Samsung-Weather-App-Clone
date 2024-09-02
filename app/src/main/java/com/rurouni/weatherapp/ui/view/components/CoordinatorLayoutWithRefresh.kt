package com.rurouni.weatherapp.ui.view.components

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.appbar.AppBarLayout
import com.rurouni.weatherapp.R

class CoordinatorLayoutWithRefresh @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {
    //Attributes
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var appBarLayout: AppBarLayout
    private var maxTranslationY: Float = 0f
    private var refreshToleration: Float = 0f
    private var duration: Long = 300L

    private lateinit var refreshData: () -> Unit

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CoordinatorLayoutWithRefresh, 0, 0)

            maxTranslationY = typedArray.getDimension(R.styleable.CoordinatorLayoutWithRefresh_coordinatorMaxTranslationY, 0f)
            refreshToleration = typedArray.getDimension(R.styleable.CoordinatorLayoutWithRefresh_coordinatorRefreshToleration, 0f)
            duration = typedArray.getInt(R.styleable.CoordinatorLayoutWithRefresh_coordinatorDuration, 300).toLong()

            typedArray.recycle()
        }
    }

    fun setValues(lottieAnimationView: LottieAnimationView, appBarLayout: AppBarLayout, refreshData: () -> Unit) {
        this.lottieAnimationView = lottieAnimationView
        this.appBarLayout = appBarLayout
        this.refreshData = refreshData
        appBarObserver()
        setOnTouch()
    }

    private var _inRefreshState = false
    private var _lastDeltaY = 0f
    private var _startY = 0f
    private var _isAppBarExpanded = false

    fun changeRefreshState(newState : Boolean) {
        _inRefreshState = newState

        if (_inRefreshState) {
            lottieAnimationView.animate()
                .translationY(maxTranslationY)
                .setDuration(duration)
                .start()
            lottieAnimationView.animate()
                .alpha(1f)
                .setDuration(duration)
                .start()
            lottieAnimationView.apply {
                repeatCount = LottieDrawable.INFINITE
                speed = 1.75f
                playAnimation()
            }
        }else{
            stopRefresh()
            stopLottieAnimation()
        }
    }

    private fun setOnTouch() {
        setOnTouchListener { _, event ->
            handleTouchEvent(event)
            true
        }
    }

    private fun handleTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                actionMove(event)
            }
            MotionEvent.ACTION_UP -> {
                actionUp(event)
            }
        }
    }

    private fun actionDown(event: MotionEvent) {
        _startY = event.y
    }

    private fun actionMove(event: MotionEvent) {
        val currentY = event.y
        val deltaY = _startY - currentY
        val translationY: Float

        translationY = if (deltaY > 0) {
            (-deltaY).coerceIn(-maxTranslationY, 0f)
        } else {
            (-deltaY).coerceIn(0f, maxTranslationY)
        }

        _lastDeltaY = translationY

        if (!_inRefreshState && _isAppBarExpanded && translationY > 0f) {
            updateAnimationPosition(translationY)
        }
    }

    private fun actionUp(event: MotionEvent) {
        _startY = 0f
        if (!_inRefreshState) {
            if (_lastDeltaY >= (maxTranslationY - refreshToleration)) {
                refreshData()
            } else {
                stopRefresh()
            }
        }
    }

    private fun stopRefresh() {
        resetAnimationPosition()
    }

    private fun updateAnimationPosition(translationY: Float) {
        val progress = (translationY / maxTranslationY).coerceIn(0f, 1f)
        val opacity = progress.coerceIn(0f, 1f)

        lottieAnimationView.alpha = opacity
        lottieAnimationView.progress = progress
        lottieAnimationView.translationY = translationY
    }

    fun resetAnimationPosition() {
        lottieAnimationView.animate()
            .translationY(0f)
            .setDuration(duration)
            .start()
        lottieAnimationView.animate()
            .alpha(0f)
            .setDuration(duration)
            .start()

        animateReverse()
    }

    private fun animateReverse() {
        val startProgress = lottieAnimationView.progress
        ObjectAnimator.ofFloat(startProgress, 0f).apply {
            this.duration = duration
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Float
                lottieAnimationView.progress = progress
            }
            start()
        }
    }

    private fun stopLottieAnimation() {
        lottieAnimationView.animate()
            .translationY(0f)
            .setDuration(duration)
            .start()
        lottieAnimationView.animate()
            .alpha(0f)
            .setDuration(duration)
            .start()
        lottieAnimationView.apply {
            repeatCount = 0
        }
    }

    private fun appBarObserver() {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val progress = Math.abs(verticalOffset / totalScrollRange.toFloat())

            if (progress <= 0.0) {
                _isAppBarExpanded = true
            }else {
                _isAppBarExpanded = false
            }
        })
    }
}