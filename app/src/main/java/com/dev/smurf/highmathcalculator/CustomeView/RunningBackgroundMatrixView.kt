package com.dev.smurf.highmathcalculator.CustomeView

import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet

//todo::rewrite this shit
class RunningBackgroundMatrixView(context : Context, attributeSet: AttributeSet?) : BackgroundMatrixView(context, attributeSet)
{
    var delay : Long = 125
    protected var isMatrixUpdatedStarted = true
    protected val updateHandler = Handler()

    private fun updateMatrix()
    {
        isMatrixUpdatedStarted = false

        for(y in numberMatrix.size-1 downTo  1)
        {
            for(x in 0 until numberMatrix[y].size)
            {
                numberMatrix[y][x] = numberMatrix [y-1][x]
            }
        }

        for(i in numberMatrix[0].indices)
        {
            numberMatrix[0][i] = rand.nextInt(range)
        }

        updateHandler.postDelayed({
        updateMatrix()},delay)

    }

    override fun onDraw(canvas: Canvas?)
    {
        if(isMatrixUpdatedStarted)updateMatrix()
            super.onDraw(canvas)
            invalidate()
    }
}