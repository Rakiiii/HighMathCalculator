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

//returns size of polynomial @polynomial rendered with partition in @ranges
//every range in @ranges represent single line of polynomial
fun Paint.getMultiLinePolynomialSize(
    polynomial: PolynomialBase,
    ranges: Array<IntRange>
): Pair<Float, Float>
{
    //check for empty polynomial or empty partition
    if (polynomial == PolynomialBase.EmptyPolynomial || polynomial.degree() == 0 || ranges.isEmpty()) return Pair(
        0.0f,
        0.0f
    )

    //width of max line
    var maxWidth = 0.0f

    //sum of height of all lines on render
    var overallHeight = 0.0f

    //get render format
    val rendFormat = polynomial.renderFormat()

    //iterate over all lines
    for (rangeIndex in ranges.indices)
    {
        //get range for iteration
        val range = ranges[rangeIndex]

        //get size of polynomial represented by range @range
        val rangeSize = getPolynomialRangeSize(polynomial, range)

        //line width
        var finalWidth = rangeSize.first + getHorizontalSpacing()

        //if it isn't last line, then some sign must be rendered
        if (rangeIndex != ranges.size - 1)
        {

            //type of sign is depends on sign of first cof in next line
            val nextCof = rendFormat[ranges[rangeIndex + 1].first].second

            //if we haven't minus before cof on next line then we must render plus
            //we have minus only if we have purely imaginary or real cof with minus
            finalWidth += if (!(nextCof.isImagination() && nextCof.im.isBeloweZero()) &&
                !(nextCof.isReal() && nextCof.re.isBeloweZero())
            )
            {

                3*getPlusWidth() + 2*getHorizontalSpacing()+30
            }
            else
            {

                3*getMinusWidth() + 2*getHorizontalSpacing()+30
            }
        }

        //if it's not first line then we must render some sign before line, so we must increase
        // width with size of sign
        if (rangeIndex != 0)
        {
            //if we do not have sign before cof, then must increase it
            val nextCof = rendFormat[ranges[rangeIndex].first].second
            finalWidth += if (!(nextCof.isImagination() && nextCof.im.isBeloweZero()) &&
                !(nextCof.isReal() && nextCof.re.isBeloweZero())
            ) getPlusWidth() + getHorizontalSpacing()
            else getMinusWidth() + getHorizontalSpacing()

        }

        //if width of this line is bigger than max width of prev lines then this line is longest
        maxWidth =
            if (finalWidth> maxWidth) finalWidth else maxWidth

        overallHeight += rangeSize.second
    }

    return Pair(maxWidth, overallHeight)
}

//wrapper for dots drawing for polynomial
fun Paint.getPolynomialSizeAsDots(polynomial: PolynomialBase) =
    if (polynomial == PolynomialBase.EmptyPolynomial || polynomial.degree() == 0) Pair(
        0.0f,
        0.0f
    )
    else getProportionalDotsInBracketSize(5)