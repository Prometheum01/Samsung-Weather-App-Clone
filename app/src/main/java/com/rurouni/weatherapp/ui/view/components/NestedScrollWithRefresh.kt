package com.rurouni.weatherapp.ui.view.components

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.widget.NestedScrollView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.google.android.material.appbar.AppBarLayout
import com.rurouni.weatherapp.R

class NestedScrollWithRefresh @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    //Attributes
    private lateinit var lottieAnimationView: LottieAnimationView
    private lateinit var appBarLayout: AppBarLayout
    private var maxTranslationY: Float = 0f
    private var refreshToleration: Float = 0f
    private var duration: Long = 300L

    private lateinit var refreshData: () -> Unit

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NestedScrollWithRefresh, 0, 0)

            maxTranslationY = typedArray.getDimension(R.styleable.NestedScrollWithRefresh_nestedScrollMaxTranslationY, 0f)
            refreshToleration = typedArray.getDimension(R.styleable.NestedScrollWithRefresh_nestedScrollRefreshToleration, 0f)
            duration = typedArray.getInt(R.styleable.NestedScrollWithRefresh_nestedScrollDuration, 300).toLong()

            typedArray.recycle()
        }
    }

    fun setValues(lottieAnimationView: LottieAnimationView, appBarLayout: AppBarLayout, refreshData: () -> Unit) {
        this.lottieAnimationView = lottieAnimationView
        this.appBarLayout = appBarLayout
        this.refreshData = refreshData
        appBarObserver()
    }

    private var _inRefreshState = false
    private var _lastDeltaY = 0f
    private var _startY = 0f
    private var _isAppBarExpanded = false
    private var _ignoreScrolling = false

    fun changeRefreshState(newState : Boolean) {
        _inRefreshState = newState
        _ignoreScrolling = false
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

    override fun onTouchEvent(event: MotionEvent): Boolean {
        handleTouchEvent(event)

        return if (!_ignoreScrolling) {
            super.onTouchEvent(event)
        } else {
            // Ignore scrolling but take hand positions
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
            _ignoreScrolling = true
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
        _ignoreScrolling = false
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

        _ignoreScrolling = false
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
        _ignoreScrolling = false
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
