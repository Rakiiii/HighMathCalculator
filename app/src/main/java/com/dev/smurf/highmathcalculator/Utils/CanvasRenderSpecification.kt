package com.dev.smurf.highmathcalculator.Utils

import android.graphics.Color
import android.graphics.Paint

class CanvasRenderSpecification
{
    companion object
    {

        //Space vertical thickness between separating line and number in fraction
        val letterVerticalSpacing = 5.0f

        val x = 2.0f

        val y = 2.0f

        val textSize = 40.0f

        val strokeWidth = 5.0f

        fun createBlackPainter() : Paint
        {
            val blackPainter = Paint(Color.BLACK)


            blackPainter.textSize = textSize

            blackPainter.strokeWidth = strokeWidth

            return blackPainter
        }

        fun getHorizontalSpacing(mPaint: Paint) : Float
        {
            return mPaint.letterSpacing*5
        }

        fun getVerticalSpaceSize(mPaint : Paint) : Float
        {
            return this.getLetterHigh(mPaint)/2
        }

        fun getHorizontalSpaceSize(mPaint: Paint) : Float
        {
            //spaces size between columns
            val arr = FloatArray(2)
            mPaint.getTextWidths("WW", arr)
            val horizontalSpaceSize = arr.sum()

            return horizontalSpaceSize
        }

        fun getLetterHigh(mPaint: Paint):Float
        {
            //font metrics
            val fm = mPaint.fontMetrics

            //high of the letter of the setted font
            val high = fm.descent - fm.ascent

            return high
        }

        fun getBracketWidth(mPaint: Paint, h : Int) : Float
        {
            return mPaint.strokeWidth * 10 * (h - 1)
        }

        fun getLineWidth(mPaint: Paint) : Float
        {
            return mPaint.strokeWidth * 3
        }
    }
}