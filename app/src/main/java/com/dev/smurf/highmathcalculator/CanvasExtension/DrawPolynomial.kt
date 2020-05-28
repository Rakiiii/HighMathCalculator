package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.PaintExtension.*
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialRoots


//draw polinom on canvas returns last point of polynomial render and vertical offset for render signs
fun Canvas.drawPolynomials(
    polynomial: PolynomialBase,
    x: Float,
    y: Float,
    mPaint: Paint
): Pair<Float, Float>
{
    val polynomialForRender = polynomial.renderFormat()

    //flag of containtment of fraction in cofs of polynomial
    var fractionFlag = false

    //check for fractions in cofs
    for (i in polynomialForRender)
    {
        if (!i.second.im.isInt() || !i.second.re.isInt())
        {
            fractionFlag = true
            break
        }
    }


    //vertical offset for symbols,signs
    val textVerticalOffset: Float = y +
            if (fractionFlag) CanvasRenderSpecification.getLetterHigh(mPaint) +
                    (CanvasRenderSpecification.getLetterHigh(mPaint) / 2) -
                    CanvasRenderSpecification.letterVerticalSpacing -
                    CanvasRenderSpecification.getLineWidth(mPaint) / 2
            else CanvasRenderSpecification.getLetterHigh(mPaint)

    //vertical offset for non fraction numbers
    val verticalOffset: Float = y +
            if (fractionFlag) CanvasRenderSpecification.getLetterHigh(mPaint) -
                    CanvasRenderSpecification.letterVerticalSpacing -
                    CanvasRenderSpecification.getLineWidth(mPaint) / 2
            else CanvasRenderSpecification.getLetterHigh(mPaint) / 2

    //horizontal offset for render those element
    var horizontalOffset = x

    //count amount of rendered cofs
    var renderedCounter = 0

    //count amount of itterations
    var itterationCounter = 0

    //start render
    for (i in polynomialForRender)
    {
        //count itteration
        itterationCounter++

        //if cof before is 0 we dont need to draw it
        if (!i.second.equals(0))
        {
            //count rendered cof
            renderedCounter++

            //if cof contains fraction we dont need any vertical offset
            if (i.second.containsFractions())
            {
                this.drawComplex(i.second, horizontalOffset, y, mPaint)
            }
            else
            {
                //if cofs dont contains fraction we need counted vertical offset
                this.drawComplex(i.second, horizontalOffset, verticalOffset, mPaint)
            }

            //update horizontal offset to the left of rendered cof
            //we dont need to render plus after last cof
            horizontalOffset += mPaint.getComplexNumberWidth(i.second)

            //init text for render after cof

            val text =
                i.first + if (i == polynomialForRender.last() || (i.second.isBelowZero())) "" else "+"

            //draw text it self
            this.drawText(text, horizontalOffset, textVerticalOffset, mPaint)

            //calculate horizontal offset
            //we need an array if size word to get length of all symbols in the word
            val arr = FloatArray(text.length)
            //get length of symbols it self
            mPaint.getTextWidths(text, arr)

            //sum symbols length,get length of symbols and update horizontal offset to the left of symbols
            horizontalOffset += arr.sum()
        }
    }

    if (itterationCounter == polynomialForRender.size && renderedCounter == 0)
    {
        //init text for render after cof
        val text = "0"

        //draw text it self
        this.drawText(text, horizontalOffset, verticalOffset + 4.0f, mPaint)

        //calculate horizontal offset
        //we need an array if size word to get length of all symbols in the word
        val arr = FloatArray(text.length)
        //get length of symbols it self
        mPaint.getTextWidths(text, arr)

        //sum symbols length,get length of symbols and update horizontal offset to the left of symbols
        horizontalOffset += arr.sum()
    }

    //returns offset pair
    return Pair(horizontalOffset, verticalOffset)
}

fun Canvas.drawPolynomial(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint) =
    drawPolynomialRange(polynomial, x, y, mPaint, 0 until polynomial.renderFormat().size)

fun Canvas.drawPolynomialRange(
    polynomial: PolynomialBase,
    x: Float,
    y: Float,
    mPaint: Paint,
    range: IntRange
)
{
    if (polynomial == PolynomialBase.EmptyPolynomial || polynomial.degree() == 0) return

    if (range.first < 0) return
    if (range.last > polynomial.renderFormat().size) return drawPolynomial(polynomial, x, y, mPaint)

    val rendFormat = polynomial.renderFormat()

    val subSize = mPaint.getPolynomialRangeSize(polynomial, range)

    val polynomialSize = Pair(subSize.first, subSize.second - mPaint.getPolynomialVerticalOffset())

    var horizontalOffset = 0.0f

    var fractionFlag = false
    //rendFormat.forEach { if (it.second.containsFractions()) fractionFlag = true }
    range.forEach { if (rendFormat[it].second.containsFractions()) fractionFlag = true }
    var extraHeightFlag = false
    range.forEach { if (rendFormat[it].first.contains('^')) extraHeightFlag = true }
    //rendFormat.forEach { if (it.first.contains('^')) extraHeightFlag = true }

    for (i in range)
    {
        val element = rendFormat[i].second
        val cofSize = mPaint.getComplexNumberSize(element)

        val variableWidth = mPaint.measureText(rendFormat[i].first)

        var cofVerticalOffset =
            ((polynomialSize.second - cofSize.second) / 2) -
                    if (fractionFlag && !element.containsFractions()) mPaint.getVerticalSpacing() else 0.0f

        val variableRect = Rect()
        mPaint.getTextBounds(rendFormat[i].first, 0, rendFormat[i].first.length, variableRect)

        val maxOfVariablesHeight = variableRect.height()


        var variableVerticalOffset =
            (maxOfVariablesHeight + (polynomialSize.second - maxOfVariablesHeight) / 2) + mPaint.getVerticalSpacing()

        val plusVerticalOffset = polynomialSize.second / 2

        if (fractionFlag)
        {
            variableVerticalOffset -= if (element.isImagination()) mPaint.strokeWidth else 2 * mPaint.getVerticalSpacing()
        }
        else
        {
            if (!extraHeightFlag) cofVerticalOffset -= mPaint.getVerticalSpacing()
        }


        drawComplex(element, x + horizontalOffset, y + cofVerticalOffset, mPaint)

        horizontalOffset += cofSize.first

        if (rendFormat[i].first != "")
        {
            drawText(rendFormat[i].first, x + horizontalOffset, y + variableVerticalOffset, mPaint)
            horizontalOffset += variableWidth
        }

        horizontalOffset += mPaint.getHorizontalSpacing()

        if (range.last != i)
        {
            if (!(rendFormat[i + 1].second.isImagination() && rendFormat[i + 1].second.im.isBeloweZero()) &&
                //if cof contain real part then it will be drawn first and we consider it's sign
                !(rendFormat[i + 1].second.isReal() && rendFormat[i + 1].second.re.isBeloweZero())
            )
            {


                drawPlus(x + horizontalOffset, y + plusVerticalOffset, mPaint)

                horizontalOffset += mPaint.getPlusWidth() + mPaint.getHorizontalSpacing()
            }
        }

    }

}

fun Canvas.drawMultiLinePolynomial(
    polynomial: PolynomialBase,
    x: Float,
    y: Float,
    mPaint: Paint,
    ranges: Array<IntRange>
)
{
    var verticalOffset = 0.0f

    val rendFormat = polynomial.renderFormat()
    val overallSize = mPaint.getMultiLinePolynomialSize(polynomial, ranges)

    for (rangeIndex in ranges.indices)
    {
        val range = ranges[rangeIndex]
        val rangeSize = mPaint.getPolynomialRangeSize(polynomial, range)

        val horizontalOffset = overallSize.first - rangeSize.first

        drawPolynomialRange(polynomial, x + horizontalOffset, y + verticalOffset, mPaint, range)

        if (rangeIndex != ranges.size - 1)
        {
            val nextCof = rendFormat[ranges[rangeIndex + 1].first].second
            if (!(nextCof.isImagination() && nextCof.im.isBeloweZero()) ||
                !(nextCof.isReal() && nextCof.re.isBeloweZero())
            )
            {
                drawPlus(
                    x + horizontalOffset + rangeSize.first + mPaint.getHorizontalSpacing(),
                    y + verticalOffset + (rangeSize.second / 2), mPaint
                )
            }
            else
            {
                drawMinus(x + horizontalOffset + rangeSize.first + mPaint.getHorizontalSpacing(),
                    y + verticalOffset + (rangeSize.second / 2), mPaint)
            }
        }

        verticalOffset += rangeSize.second
    }

}

//draw equation that setted by polynomial @polynomial on canvas
fun Canvas.drawEquation(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint)
{
    //draw polynomial
    val polynomialSize = mPaint.getPolynomialSize(polynomial)
    this.drawPolynomial(polynomial, x, y, mPaint)

    //draw equals zero after polynomial to get equation
    val verticalOffset =
        mPaint.getBaseHeight() + (polynomialSize.second - mPaint.getBaseHeight()) / 2

    this.drawText(
        "=0",
        x + polynomialSize.first + mPaint.getHorizontalSpacing(),
        y + verticalOffset,
        mPaint
    )
}


fun Canvas.drawPolynomialRoots(roots: PolynomialRoots, x: Float, y: Float, mPaint: Paint)
{
    var verticalOffset = y
    for (i in roots)
    {
        if (i.second.containsFractions())
        {
            verticalOffset =
                y + CanvasRenderSpecification.getLetterHigh(mPaint) + CanvasRenderSpecification.getVerticalSpaceSize(
                    mPaint
                ) + CanvasRenderSpecification.getLineWidth(mPaint) / 2

            break
        }
    }

    var horizontalOffset = x

    for (i in roots)
    {

        val symbol = i.first + roots.getSepareteSymbol()
        val arr = FloatArray(symbol.length)
        mPaint.getTextWidths(symbol, arr)

        this.drawText(symbol, horizontalOffset, verticalOffset, mPaint)

        horizontalOffset += arr.sum()

        if (i.second.containsFractions())
        {
            this.drawComplex(i.second, horizontalOffset, y, mPaint)
        }
        else
        {
            this.drawComplex(i.second, horizontalOffset, verticalOffset, mPaint)
        }

        horizontalOffset += mPaint.getComplexNumberWidth(i.second)

        horizontalOffset += CanvasRenderSpecification.getHorizontalSpaceSize(mPaint)

    }

}
