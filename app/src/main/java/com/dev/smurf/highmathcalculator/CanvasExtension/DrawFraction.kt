package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.PaintExtension.*
import kotlin.math.absoluteValue


//Extension for canvas to draw custom fraction returns offset of middle of fraction
fun Canvas.drawFraction(fraction: Fraction, x: Float, y: Float, mPaint: Paint): Float
{

    //thicknes of separating line in fraction
    val lineThickness = mPaint.strokeWidth

    //horizontal offset of fraction
    var horizontalOffset = 0.0f

    //cof for rendering lower in fraction
    val lowerOffsetCof = 0.7f

    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)

    when
    {
        //Drawing number in case of non fractoin value
        fraction.isInt() ->
        {

            //counting vertical offsetn to draw number in the middle of possible fraction
            val verticalOffset =
                (2 * high + lineThickness + 2 * CanvasRenderSpecification.letterVerticalSpacing) / 4

            //drawing number
            this.drawText(
                fraction.upper.toString(),
                x + horizontalOffset,
                y + verticalOffset,
                mPaint
            )
            return verticalOffset * 2
        }
        //Drawing fraction with minus
        fraction.isBeloweZero() ->
        {
            //Drawing minus
            val minusVerticalPozition =
                y + high + CanvasRenderSpecification.letterVerticalSpacing * 3
            this.drawText("-", x + horizontalOffset, minusVerticalPozition, mPaint)

            //getting width of minus in setted offset
            val arr = FloatArray(2)
            mPaint.getTextWidths("-", arr)

            //increment horizontal offset on minus size
            horizontalOffset += arr[0] + CanvasRenderSpecification.defspaceSize
        }
    }

    //vertical offset of y for separating line position
    val lineVerticalPosition = y + high + CanvasRenderSpecification.letterVerticalSpacing

    //array with length of characters in upper number of fraction
    val upperValueWidth = FloatArray(fraction.upper.toString().length)
    //array with length of characters in lower number of fraction
    val lowerValueWidth = FloatArray(fraction.lower.toString().length)

    //width of separating line
    val lineWidth: Float
    //offset of upper number in fraction
    var upperOffset = 0.0f
    //offset of lower number in fraction
    var lowerOffset = 0.0f

    //counting max length of number in fraction
    if (mPaint.getTextWidths(
            fraction.upper.toString(),
            upperValueWidth
        ) > mPaint.getTextWidths(fraction.lower.toString(), lowerValueWidth)
    )
    {
        //if upper number longer then linew width is width of upper number
        lineWidth = upperValueWidth.sum()

        //in this case lower number must start from some offset to in the middle of line
        lowerOffset = (upperValueWidth.sum() - lowerValueWidth.sum()) / 2
    }
    else
    {
        //if lower number longer then linew width is width of upper number
        lineWidth = lowerValueWidth.sum()

        //in this case upper number must start from some offset to in the middle of line
        upperOffset = (lowerValueWidth.sum() - upperValueWidth.sum()) / 2
    }


    //draw separating line
    this.drawLine(
        x + horizontalOffset,
        lineVerticalPosition,
        x + horizontalOffset + lineWidth,
        lineVerticalPosition,
        mPaint
    )

    //draw upper number
    this.drawText(
        fraction.upper.absoluteValue.toString(),
        x + horizontalOffset + upperOffset,
        lineVerticalPosition - CanvasRenderSpecification.letterVerticalSpacing,
        mPaint
    )

    //draw lower number
    this.drawText(
        fraction.lower.toString(),
        x + horizontalOffset + lowerOffset,
        lineVerticalPosition + high * lowerOffsetCof,
        mPaint
    )

    return lineVerticalPosition
}


fun Canvas.drawFractions(fraction: Fraction, x: Float, y: Float, mPaint: Paint)
{
    //save align state
    val align = mPaint.textAlign

    val size = mPaint.getFractionSize(fraction)

    //set left align state for render
    mPaint.textAlign = Paint.Align.LEFT
    if (fraction.isInt())
    {
        this.drawText(fraction.upper.toString(), x, y + size.second, mPaint)
    }
    else
    {
        val overallHorizontalOffset =
            if (fraction.isBeloweZero()) mPaint.getMinusWidth() + mPaint.getHorizontalSpacing() else 0.0f


        val upperHorizontalOffset = overallHorizontalOffset +
                ((size.first - overallHorizontalOffset) - mPaint.getFractionUpperWidth(fraction)) / 2
        val lowerHorizontalOffset = overallHorizontalOffset +
                ((size.first - overallHorizontalOffset) - mPaint.getFractionLowerWidth(fraction)) / 2


        val lineVerticalOffset = mPaint.getBaseHeight() + mPaint.getVerticalSpacing()*2

        if (fraction.isBeloweZero())
        {
            drawMinus(x, y + lineVerticalOffset, mPaint)
        }

        drawLine(
            x + overallHorizontalOffset,
            y + lineVerticalOffset,
            x + size.first,
            y + lineVerticalOffset,
            mPaint
        )

        drawText(
            fraction.upper.absoluteValue.toString(),
            x + upperHorizontalOffset,
            y + mPaint.getBaseHeight(),
            mPaint
        )

        drawText(
            fraction.lower.absoluteValue.toString(),
            x + lowerHorizontalOffset,
            y + mPaint.getBaseHeight() + mPaint.getBaseHeight(),
            mPaint
        )

    }
    //restore align state
    mPaint.textAlign = align
}

//draw minus sign  as line
fun Canvas.drawMinus(x: Float, y: Float, mPaint: Paint)
{
    val rect = Rect()
    mPaint.getTextBounds("-", 0, 1, rect)

    drawLine(x, y, x + rect.width(), y, mPaint)
}

//draw plus sign as line compose
fun Canvas.drawPlus(x: Float, y: Float, mPaint: Paint)
{
    val size = mPaint.getPlusSize()

    drawLine(x, y, x + mPaint.measureText("+"), y, mPaint)
    drawLine(
        x + (size.first / 2),
        y - (size.second / 2),
        x + (size.first / 2),
        y + (size.second / 2),
        mPaint
    )
}
