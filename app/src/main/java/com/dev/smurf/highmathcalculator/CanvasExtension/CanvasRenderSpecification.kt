package com.dev.smurf.highmathcalculator.CanvasExtension

import android.app.Application
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import com.dev.smurf.highmathcalculator.CalculatorApplication
import com.dev.smurf.highmathcalculator.R
import java.lang.reflect.Type

class CanvasRenderSpecification
{
    companion object
    {

        //Space vertical thickness between separating line and number in fraction
        const val letterVerticalSpacing = 5.0f

        const val textSize = 40.0f

        const val biggerTextSize = 60.0f

        const val strokeWidth = 2.0f

        const val defspaceSize = 5.0f

        const val PolynomialTopMargin = 6.0f
        const val PolynomialBottomMargin = 6.0f

        fun createRedPainterWithUnderline() : Paint
        {
            val redPainter = Paint(Color.RED)

            redPainter.color = Color.RED

            redPainter.typeface = Typeface.SERIF

            redPainter.textSize = biggerTextSize

            redPainter.flags = Paint.UNDERLINE_TEXT_FLAG
            redPainter.flags = Paint.FAKE_BOLD_TEXT_FLAG

            return redPainter
        }

        fun createBlackPainter() : Paint
        {
            val blackPainter = Paint(Color.BLACK)
            //blackPainter.color = CalculatorApplication.context.getColor(R.color.darker_gray)
            blackPainter.color = Color.GRAY
            blackPainter.typeface = Typeface.SERIF


            blackPainter.textSize =
                textSize

            blackPainter.strokeWidth =
                strokeWidth

            return blackPainter
        }

        fun getVerticalSpaceSize(mPaint : Paint) : Float
        {
            return getLetterHigh(
                mPaint
            ) /2
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


        fun getLineWidth(mPaint: Paint) : Float
        {
            return mPaint.strokeWidth * 3
        }
    }
}