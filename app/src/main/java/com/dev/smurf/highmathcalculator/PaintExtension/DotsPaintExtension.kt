package com.dev.smurf.highmathcalculator.PaintExtension

import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification


fun Paint.getDotsSpaceSize(radius: Float) = radius / 2

fun Paint.getDotsLineOverflow(radius: Float) = radius / 3

//return dots radius proportional to letter size
fun Paint.getProportionalDotsRadius(): Float
{
    val bracketSize = Rect()
    getTextBounds("(", 0, 1, bracketSize)
    return bracketSize.height().toFloat() / 4
}

//return size of rendered @amount amount of dots
fun Paint.getDotsSize(amount: Int, radius: Float): Pair<Float, Float>
{
    val width = amount.toFloat() * (radius * 2) + (amount - 1).toFloat() * getDotsSpaceSize(radius)
    val height = radius * 2
    return Pair(width, height)
}

//return size of rendered @amount amount of dots with brackets
fun Paint.getDotsInBracketSize(amount: Int, radius: Float): Pair<Float, Float>
{
    //get size of left bracket
    val leftBracketSize = Rect()
    getTextBounds("(", 0, 1, leftBracketSize)

    //get size of right bracket
    val rightBracketSize = Rect()
    getTextBounds("(", 0, 1, rightBracketSize)

    //get size of dots
    val dotsSize = getDotsSize(amount, radius)


    return Pair(
        dotsSize.first + leftBracketSize.width().toFloat() +rightBracketSize.width().toFloat()+getDotsSpaceSize(radius)*2,
        if (dotsSize.second > leftBracketSize.height().toFloat()) dotsSize.second
        else leftBracketSize.height().toFloat()
    )
}

//return size of dots in brackets with propoesional radius
fun Paint.getProportionalDotsInBracketSize(amount: Int) =
    getDotsInBracketSize(amount, getProportionalDotsRadius())

//return size of proportional dots in lines size
fun Paint.getProportionalDotsInLineSize(amount: Int, radius: Float): Pair<Float, Float>
{
    val spaceSize = getDotsSpaceSize(radius)
    val lineWidth = CanvasRenderSpecification.getLineWidth(this)
    val lineOverflow = getDotsLineOverflow(radius)
    val dotsSize = getDotsSize(amount, radius)

    return Pair(dotsSize.first + lineWidth * 2 + spaceSize * 2, dotsSize.second + lineOverflow * 2)
}