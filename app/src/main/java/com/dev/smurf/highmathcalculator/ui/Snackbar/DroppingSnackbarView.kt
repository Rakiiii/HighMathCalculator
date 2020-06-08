package com.dev.smurf.highmathcalculator.ui.Snackbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dev.smurf.highmathcalculator.R
import com.google.android.material.snackbar.ContentViewCallback

class DroppingSnackbarView
@JvmOverloads
constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback
{
    var progressBarWithCountDown: ProgressBar
    var progressBarPercenteTextView: TextView
    var button: Button
    var message: TextView
    var mainLayout: ConstraintLayout
    var progressLayout: RelativeLayout

    var contentInAnimatorSet: AnimatorSet

    var contentOutAnimatorSet = AnimatorSet()

    init
    {
        View.inflate(context, R.layout.dropping_snackbar_view, this)
        clipToPadding = false
        progressBarPercenteTextView = findViewById(R.id.snackbarTVProgressBarCountDown)
        progressBarWithCountDown = findViewById(R.id.snackbarProgressBar)
        button = findViewById(R.id.snackbarBtn)
        message = findViewById(R.id.snackbarTextView)
        mainLayout = findViewById(R.id.snackbarMainLayout)
        progressLayout = findViewById(R.id.snackbarProgressBarLayout)

        val scaleX = ObjectAnimator.ofFloat(mainLayout, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(mainLayout, View.SCALE_Y, 0f, 1f)
        contentInAnimatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
    }

    override fun animateContentIn(delay: Int, duration: Int)
    {

        contentInAnimatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int)
    {
        contentOutAnimatorSet.start()
    }

    /*
    override fun animateContentIn(delay: Int, duration: Int)
    {
        val point = Point()
        context.windowManager.defaultDisplay.getSize(point)
        val dropAnimator = ObjectAnimator.ofFloat(
            snackbarMainLayout,
            View.TRANSLATION_Y,
            -1f,
            point.y.toFloat() - mainLayout.height
        )
        val animatorSet = AnimatorSet().apply {
            interpolator = BounceInterpolator()
            setDuration(1500)
            play(dropAnimator)
        }

        animatorSet.start()
    }

    override fun animateContentOut(delay: Int, duration: Int)
    {
        val scaleAnimation = ObjectAnimator.ofFloat(
            snackbarMainLayout,
            View.SCALE_Y, 1f, 0f
        )
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(1500)
            play(scaleAnimation)
        }

        animatorSet.start()
    }*/
}