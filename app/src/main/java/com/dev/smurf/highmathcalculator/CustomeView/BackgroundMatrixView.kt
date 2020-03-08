package com.dev.smurf.highmathcalculator.CustomeView

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.dev.smurf.highmathcalculator.Numbers.isOdd
import com.dev.smurf.highmathcalculator.Utils.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Utils.NumberRepresenter

open class BackgroundMatrixView : View
{
    protected val range = 2
    protected var viewWidth = 0
    protected var viewHigh = 0
    protected val scale = 3
    val numberbHigh = NumberRepresenter.numberHigh
    val numberWidth = NumberRepresenter.numberWidth

    val rand = java.util.Random()

    protected var rightOffset = 0
    protected var leftOffset = 0
    protected var upperOffset = 0



    protected lateinit var matrixBitmap : Bitmap

    protected var numberRepresentation : Array<IntArray> = Array(10) { IntArray(numberWidth*numberbHigh) }

    protected lateinit var numberMatrix : Array<IntArray>

    constructor(context : Context , attributeSet: AttributeSet?) : super(context,attributeSet)
    {
        for(i in 0..range)
        {
            numberRepresentation[i] = NumberRepresenter.GetNumberRepresentation(i)
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int)
    {
        super.onSizeChanged(w, h, oldw, oldh)

        viewHigh = h
        viewWidth = w

        matrixBitmap = Bitmap.createBitmap(viewWidth/scale , viewHigh/scale , Bitmap.Config.RGB_565)

        leftOffset =  (w/scale % numberWidth)/2 + if((w/scale % numberWidth).isOdd()) 1 else 0
        rightOffset = (w/scale % numberWidth)/2
        upperOffset = (h/scale % numberbHigh)/2

        numberMatrix = Array(viewHigh/(numberbHigh*scale)){IntArray(viewWidth/(numberWidth*scale)){rand.nextInt(range)}}
    }



    private fun drawMatrix(canvas: Canvas?)
    {
        var xCord = leftOffset
        var yCord = upperOffset
        for(y in numberMatrix)
        {
            for(x in y)
            {
                matrixBitmap.setPixels(numberRepresentation[x] , 0 , numberWidth , xCord , yCord , numberWidth , numberbHigh)
                xCord += numberWidth
            }
            xCord = leftOffset
            yCord += numberbHigh
        }

        canvas?.scale(scale.toFloat(),scale.toFloat())
        canvas?.drawBitmap(matrixBitmap,0.0f, 0.0f, CanvasRenderSpecification.createBlackPainter())
    }

    override fun onDraw(canvas: Canvas?)
    {
        drawMatrix(canvas)
    }
}