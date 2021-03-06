package com.dev.smurf.highmathcalculator.PaintExtension

import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import kotlin.math.absoluteValue


//returns length of @fraction in paint param
@Deprecated("Use getFractionSize(@fraction).first instead due to better realization")
fun Paint.getFractionWidth(fraction: Fraction): Float
{
    //overall length of fraction
    var overallLength = 0.0f

    //if fraction is not a fraction
    if (fraction.isInt())
    {
        //counting length of upper number
        val arr = FloatArray(fraction.upper.toString().length)
        this.getTextWidths(fraction.upper.toString(), arr)
        return arr.sum()
    }


    //if fraction contains minus symbol
    if (fraction.isBeloweZero())
    {
        //counting length of minus symbol
        val arr = FloatArray(1)
        this.getTextWidths("-", arr)

        //increase overall length with length of minus symbol
        overallLength += arr.sum()
    }

    //array with length of characters in upper number of fraction
    val upperValueWidth = FloatArray(fraction.upper.toString().length)
    //array with length of characters in lower number of fraction
    val lowerValueWidth = FloatArray(fraction.lower.toString().length)

    //counting max length of number in fraction
    if (this.getTextWidths(
            fraction.upper.toString(),
            upperValueWidth
        ) > this.getTextWidths(fraction.lower.toString(), lowerValueWidth)
    )
    {
        //if upper number longer then increase overall length with its length
        overallLength += upperValueWidth.sum()

    }
    else
    {
        //if lower number longer then increase overall length with its length
        overallLength += lowerValueWidth.sum()

    }

    return overallLength
}

@Deprecated("Dont use it, work work with fraction like you work with rectangle")
fun Paint.getFractionLineVerticalOffset(fraction: Fraction): Float
{
    if (fraction.isInt()) return (2 * CanvasRenderSpecification.getLetterHigh(this) + CanvasRenderSpecification.getLineWidth(
        this
    ) + 2 * CanvasRenderSpecification.letterVerticalSpacing) / 2

    return CanvasRenderSpecification.getLetterHigh(this) + CanvasRenderSpecification.letterVerticalSpacing
}

fun Paint.getVerticalSpacing() = this.strokeWidth * 2

fun Paint.getHorizontalSpacing() = this.measureText(" ")/4

fun Paint.getFractionUpperWidth(fraction: Fraction) =
    this.measureText(fraction.upper.absoluteValue.toString())

fun Paint.getFractionLowerWidth(fraction: Fraction) =
    this.measureText(fraction.lower.absoluteValue.toString())

fun Paint.getMinusWidth() : Float
{
    return measureText("-")
}

fun Paint.getPlusWidth() : Float
{

    return measureText("+")
}

fun Paint.getBaseHeight() : Float
{
    return fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading
}

fun Paint.getPlusSize():Pair<Float,Float>
{
    val rect = Rect()
    getTextBounds("+", 0, 1, rect)
    return Pair(measureText("+"),rect.height().toFloat())
}

fun Paint.getFractionSize(fraction: Fraction): Pair<Float, Float>
{
    if (fraction.isInt())
    {
        val height = getBaseHeight()

        val width = measureText(fraction.upper.toString())

        return Pair(width, height)
    }

    var maxWidth =
        if (getFractionUpperWidth(fraction) > getFractionLowerWidth(fraction)) getFractionUpperWidth(fraction)
        else getFractionLowerWidth(fraction)

    maxWidth += if (fraction.isBeloweZero()) getMinusWidth() + getHorizontalSpacing() else 0.0f


    var maxHeight = 0.0f

    maxHeight += this.strokeWidth + (this.getVerticalSpacing() * 2) + getBaseHeight()*2

    return Pair(maxWidth, maxHeight)
}

