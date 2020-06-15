package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.PaintExtension.getDotsLineOverflow
import com.dev.smurf.highmathcalculator.PaintExtension.getDotsSize
import com.dev.smurf.highmathcalculator.PaintExtension.getDotsSpaceSize
import com.dev.smurf.highmathcalculator.PaintExtension.getProportionalDotsRadius

fun Canvas.drawDots(amount: Int, x: Float, y: Float, radius: Float, mPaint: Paint)
{
    //space size between dots
    val spaceSize = mPaint.getDotsSpaceSize(radius)

    //dots central  position
    val verticalOffset = y + radius
    var horizontalOffset = x + radius

    //draw dots
    for (i in 0 until amount)
    {
        drawCircle(horizontalOffset, verticalOffset, radius, mPaint)
        horizontalOffset += radius * 2 + spaceSize
    }
}

fun Canvas.drawDotsInBrackets(amount: Int, x: Float, y: Float, radius: Float, mPaint: Paint)
{
    val spaceSize = mPaint.getDotsSpaceSize(radius)
    val leftBracketSize = Rect()
    mPaint.getTextBounds("(", 0, 1, leftBracketSize)

    var horizontalOffset = x
    val verticalOffset = y + (leftBracketSize.height().toFloat() - radius*2)/2

    drawText("(", horizontalOffset, y + leftBracketSize.height(), mPaint)
    horizontalOffset += spaceSize + leftBracketSize.width()


    drawDots(amount, horizontalOffset, verticalOffset, radius, mPaint)

    val dotsSize = mPaint.getDotsSize(amount, radius)

    horizontalOffset += dotsSize.first + spaceSize

    drawText(")", horizontalOffset, y + leftBracketSize.height(), mPaint)
}

fun Canvas.drawProporsionalDotsInBrackets(amount: Int, x: Float, y: Float, mPaint: Paint) =
    drawDotsInBrackets(amount, x, y, mPaint.getProportionalDotsRadius(), mPaint)


fun Canvas.drawProporsionalDotsInLines(
    amount: Int,
    x: Float,
    y: Float,
    radius: Float,
    mPaint: Paint
)
{
    val lineLength = mPaint.getDotsLineOverflow(radius) * 2 + radius * 2
    val spaceSize = mPaint.getDotsSpaceSize(radius)
    var horizontalOffset = x
    val verticalOffset = y + mPaint.getDotsLineOverflow(radius)
    //draw left line
    drawLine(horizontalOffset, y, horizontalOffset, y + lineLength, mPaint)

    horizontalOffset += spaceSize

    drawDots(amount, horizontalOffset, verticalOffset, radius, mPaint)

    horizontalOffset += spaceSize + mPaint.getDotsSize(amount, radius).first

    //draw right line
    drawLine(horizontalOffset, y, horizontalOffset, y + lineLength, mPaint)
}