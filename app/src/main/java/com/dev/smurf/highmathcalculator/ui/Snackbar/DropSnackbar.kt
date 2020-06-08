package com.dev.smurf.highmathcalculator.ui.Snackbar

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.R
import com.dev.smurf.highmathcalculator.ViewExtension.findSuitableParent
import com.google.android.material.snackbar.BaseTransientBottomBar
import kotlinx.coroutines.*
import org.jetbrains.anko.textColor

class DropSnackbar private constructor(
    parent: ViewGroup,
    content: DroppingSnackbarView
) : BaseTransientBottomBar<DropSnackbar>(parent, content, content)
{
    private val snackbarView = content

    init
    {
        getView().setBackgroundColor(CalculatorApplication.context.getColor(android.R.color.transparent))
        getView().setPadding(0, 0, 0, 6)
    }

    companion object
    {
        fun make(view: View): DropSnackbar
        {
            val parent = view.findSuitableParent()
                ?: throw IllegalStateException("No suitable parent found from given view")

            val sbView = LayoutInflater.from(view.context).inflate(
                R.layout.drop_snackbar_layout,
                parent,
                false
            ) as DroppingSnackbarView

            return DropSnackbar(parent, sbView)
        }
    }

    fun setButton(text: String, color: Int = Color.BLACK, action: () -> Unit): DropSnackbar
    {
        snackbarView.button.text = text

        snackbarView.button.textColor = color

        snackbarView.button.setOnClickListener { action() }

        snackbarView.button.visibility = View.VISIBLE

        return this
    }

    fun setMessage(text: String, color: Int = Color.BLACK, action: () -> Unit = {}): DropSnackbar
    {
        snackbarView.message.text = text
        snackbarView.message.textColor = color
        snackbarView.message.setOnClickListener { action() }
        snackbarView.message.visibility = View.VISIBLE

        return this
    }

    fun setBackground(background: Drawable): DropSnackbar
    {
        snackbarView.mainLayout.background = background
        return this
    }

    fun setProgressBar(
        textColor: Int = Color.BLACK,
        progressDrawable: Drawable = CalculatorApplication.context.getDrawable(R.drawable.circular)!!
    ): DropSnackbar
    {
        snackbarView.progressBarWithCountDown.visibility = View.VISIBLE
        snackbarView.progressBarWithCountDown.progressDrawable = progressDrawable

        //snackbarView.progressBarWithCountDown.isIndeterminate = false


        snackbarView.progressBarPercenteTextView.visibility = View.VISIBLE
        snackbarView.progressBarPercenteTextView.textColor = textColor


        return this
    }

    override fun show()
    {
        val ioScope = CoroutineScope(Dispatchers.IO)
        val uiScope = CoroutineScope(Dispatchers.Main)

        super.show()

        ioScope.launch {
            val progressBarDelay = (duration / 100).toLong()
            for (i in 0..100)
            {
                uiScope.launch { snackbarView.progressBarWithCountDown.setProgress(i, true) }
                delay(progressBarDelay)
            }
        }

        ioScope.launch {
            val timeDelay = (duration / 1000)
            if (timeDelay == 0)
            {
                snackbarView.progressBarPercenteTextView.visibility = View.GONE;return@launch
            }
            for (i in timeDelay downTo 0)
            {
                uiScope.launch { snackbarView.progressBarPercenteTextView.text = i.toString() }
                delay(1000)
            }
        }
    }
}