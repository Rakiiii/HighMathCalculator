package com.dev.smurf.highmathcalculator.PaintExtension

import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase


@Deprecated("Use Paint.getPolynomialSize(@polynomial).first instead")
fun Paint.getPolynomialWidth(polynomial: PolynomialBase): Float
{
    val renderSet = polynomial.renderFormat()

    var overallWidth = 0.0f

    for (i in renderSet)
    {
        overallWidth += this.getComplexNumberWidth(i.second)

        val text = i.first + "+"

        val arr = FloatArray(text.length)
        this.getTextWidths(text, arr)
        overallWidth += arr.sum()
    }



    return overallWidth
}

@Deprecated("Use Paint.getPolynomialSize(@polynomial).second instead")
fun Paint.getPolynomialHigh(polynomial: PolynomialBase): Float
{
    for (i in polynomial.renderFormat())
    {
        if (i.second.containsFractions()) return CanvasRenderSpecification.getLetterHigh(this) + 2 * CanvasRenderSpecification.getVerticalSpaceSize(
            this
        ) + CanvasRenderSpecification.getLineWidth(this)
    }

    val textRect = Rect()
    this.getTextBounds(
        polynomial.renderFormat()[0].first,
        0,
        polynomial.renderFormat()[0].first.length,
        textRect
    )

    return 2 * textRect.height()
        .toFloat() + CanvasRenderSpecification.PolynomialBottomMargin + CanvasRenderSpecification.PolynomialTopMargin
    //return CanvasRenderSpecification.getLetterHigh(this)
}

fun Paint.getPolynomialVerticalOffset() = getVerticalSpacing() * 3


/* returns size of rectangle in which polynomial will be drawn */
fun Paint.getPolynomialSize(polynomial: PolynomialBase): Pair<Float, Float>
{
    return getPolynomialRangeSize(polynomial, 0 until polynomial.renderFormat().size)
}

fun Paint.getPolynomialRangeSize(polynomial: PolynomialBase, range: IntRange): Pair<Float, Float>
{
    if (polynomial == PolynomialBase.EmptyPolynomial || polynomial.degree() == 0) return Pair(
        0.0f,
        0.0f
    )
    if (range.first < 0) return Pair(0.0f, 0.0f)

    /*
    polynomial render is fully describe by render of pairs of cof and variable
    polynomial render format describe ArrayList of such pair
    where first is variable and second is cof
    */

    val rendFormat = polynomial.renderFormat()
    if (range.last > rendFormat.size - 1) return getPolynomialSize(polynomial)

    //overall width of rendered polynomial
    var overallWidth = 0.0f
    //height of highest element in polynomial render format
    var maxHeight = 0.0f

    for (i in range)
    {
        //get size of cofs
        val cofSize = getComplexNumberSize(rendFormat[i].second)

        //add cof width to overall width
        overallWidth += cofSize.first + measureText(rendFormat[i].first)

        /*
        if we render non terminate polynomial pair, then we must render the sign
        if cof is purly imaginary then only im part will be drawn, so if it's below zero then minus will be draw,
        else plus will be drawn, minus size is counted inside size of complex number, but plus will be rendered implicitly
        */
        if (i != range.last)
        {
            if (!(rendFormat[i + 1].second.isImagination() && rendFormat[i + 1].second.im.isBeloweZero()) &&
                //if cof contain real part then it will be drawn first and we consider it's sign
                !(rendFormat[i + 1].second.isReal() && rendFormat[i + 1].second.re.isBeloweZero())
            )
            {
                //size of plus sign
                overallWidth += getPlusWidth()
                //size of space between next cof and sign
                overallWidth += getHorizontalSpacing()

            }
        }

        /*
        plus between this cof and sign or end of bitmap
        for more smooth render
        */
        overallWidth += getHorizontalSpacing()

        //if cof height is bigger then height of previous highest elemnt , then this cof is highest
        maxHeight = if (cofSize.second > maxHeight) cofSize.second else maxHeight

        /*
        count size of variable by two way and check biggest
        need this trick in reason of stupid canvas symbols height rounding
        */
        val variableRect = Rect()
        getTextBounds(rendFormat[i].first, 0, rendFormat[i].first.length, variableRect)

        val maxOfVariablesHeight =
            if (variableRect.height().toFloat() > getBaseHeight()) variableRect.height()
                .toFloat()
            else getBaseHeight()

        maxHeight = if (maxOfVariablesHeight > maxHeight) maxOfVariablesHeight else maxHeight
    }

    return Pair(overallWidth, maxHeight + getPolynomialVerticalOffset())
}

fun Paint.getMultiLinePolynomialSize(
    polynomial: PolynomialBase,
    ranges: Array<IntRange>
): Pair<Float, Float>
{
    var maxWidth = 0.0f
    var overallHeight = 0.0f

    val rendFormat = polynomial.renderFormat()

    for (rangeIndex in ranges.indices)
    {
        val range = ranges[rangeIndex]
        val rangeSize = getPolynomialRangeSize(polynomial, range)

        var finalWidth = rangeSize.first + getHorizontalSpacing()
        if (rangeIndex != ranges.size - 1)
        {
            val nextCof = rendFormat[ranges[rangeIndex + 1].first].second
            finalWidth += if (!(nextCof.isImagination() && nextCof.im.isBeloweZero()) ||
                !(nextCof.isReal() && nextCof.re.isBeloweZero())
            )
            {
                getPlusWidth() + getHorizontalSpacing()
            }
            else
            {
                getMinusWidth() + getHorizontalSpacing()
            }
        }

        maxWidth =
            if (rangeSize.first > maxWidth) rangeSize.first else maxWidth

        overallHeight += rangeSize.second
    }

    return Pair(maxWidth,overallHeight)
}