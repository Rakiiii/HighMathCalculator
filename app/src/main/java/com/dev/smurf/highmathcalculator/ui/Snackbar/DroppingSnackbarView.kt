package com.dev.smurf.highmathcalculator.ui.Snackbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Point
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.CalculatorApplication.Companion.context
import com.dev.smurf.highmathcalculator.R
import com.google.android.material.snackbar.ContentViewCallback
import org.jetbrains.anko.windowManager

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
    var layoutForAnimation : LinearLayout

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
        layoutForAnimation = findViewById(R.id.layoutForAnimation)



        layoutForAnimation
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


    fun getDropAnimator(): AnimatorSet
    {
        val point = Point()
        context.windowManager.defaultDisplay.getSize(point)
        Log.d("snackbar@","screen size ${point.y}")
        //set snackbar size for half of screen for animation
        val params = layoutForAnimation.layoutParams
        val startHeight = progressBarWithCountDown.layoutParams.height
        params.height = point.y
        layoutForAnimation.layoutParams = params
        layoutForAnimation.requestLayout()


        //set start position for drop
        val goUpperAnimator = ObjectAnimator.ofFloat(mainLayout, View.TRANSLATION_Y, -point.y.toFloat()/2)
        goUpperAnimator.apply {
            duration = 0
            interpolator = OvershootInterpolator()
        }

        //drop it self
        val dropWithBounceAnimation = ObjectAnimator.ofFloat(
            mainLayout,
            View.TRANSLATION_Y,
            -startHeight.toFloat()
        )
        dropWithBounceAnimation.apply {
            duration = 1500
            interpolator = BounceInterpolator()
        }

        //last step to right position
        val extraDrop = ObjectAnimator.ofFloat(
            mainLayout,
            View.TRANSLATION_Y,
            0f
        )

        extraDrop.apply {
            duration = 0
            interpolator = OvershootInterpolator()
        }



        val animatorSet = AnimatorSet().apply {
            playSequentially(goUpperAnimator,dropWithBounceAnimation,extraDrop)
        }
        animatorSet.addListener(onEnd = {
            //set original size of layout after animations
        val subParams = layoutForAnimation.layoutParams
            subParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutForAnimation.layoutParams = subParams
            layoutForAnimation.requestLayout()
        })
        return animatorSet
    }

}