package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.PaintExtension.*
import kotlin.math.absoluteValue

//Extension for canvas to draw complex number
@Deprecated("Use drawComplex instead")
fun Canvas.drawComplexx(complexNumber: ComplexNumber, x: Float, y: Float, mPaint: Paint) //: Float
{

    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)
    when
    {
        //if number purely real then draw only real part
        complexNumber.isReal() ->
        {
            //if real number render real part
            this.drawFraction(complexNumber.re, x, y, mPaint)

            return
        }

        //if number purly imaginary draw only imageronary part
        complexNumber.isImagination() ->
        {
            //if imegination number render imeginary part and count i pos
            val iVerticalPos = y + this.drawFraction(
                complexNumber.im,
                x,
                y,
                mPaint
            ) - high / 2 - CanvasRenderSpecification.letterVerticalSpacing

            val iHorizontalPos = x + mPaint.getFractionWidth(complexNumber.im)

            this.drawText("i", iHorizontalPos, iVerticalPos, mPaint)

            return //iVerticalPos - high/2
        }
    }

    /*
     * first of all we must draw re and im fractions part to get positions of separation line
     * then in logic of re and im part we must get biggest offset
    */


    //counting length of left bracket
    val arr = FloatArray(1)
    mPaint.getTextWidths("(", arr)
    val lengthOfLeftBracket = arr.sum()

    //move horizontal position for drawing to the left of bracket
    var horizontalPosition = x + lengthOfLeftBracket

    //draw real part of number and get offset of middle of drawed number
    val middlePositionOfRealPart =
        this.drawFraction(
            complexNumber.re,
            horizontalPosition,
            y + if (complexNumber.re.isInt() && !complexNumber.im.isInt()) mPaint.getFractionLineVerticalOffset(
                complexNumber.im
            ) - high / 2
            else 0.0f,
            mPaint
        )

    //move horizontal position to the left of real part
    horizontalPosition += mPaint.getFractionWidth(complexNumber.re)

    //calculate vertical position of left bracket and sign
    val verticalPositionOfSign = when
    {
        complexNumber.re.isInt() && complexNumber.im.isInt() ->
            y + middlePositionOfRealPart - high / 2 - CanvasRenderSpecification.letterVerticalSpacing
        else ->
            y + if (mPaint.getFractionLineVerticalOffset(complexNumber.re) >= mPaint.getFractionLineVerticalOffset(
                    complexNumber.im
                )
            ) middlePositionOfRealPart
            else mPaint.getFractionLineVerticalOffset(complexNumber.im)
    }
    //draw left bracket
    this.drawText("(", x, verticalPositionOfSign, mPaint)

    //if imaginary part is below zero
    if (complexNumber.im.isBeloweZero())
    {
        //draw minus
        this.drawText("-", horizontalPosition, verticalPositionOfSign, mPaint)

        //getting width of minus symbol
        mPaint.getTextWidths("-", arr)

        //move horizontal position to the left of minus
        horizontalPosition += arr.sum()
    }
    else
    {
        //draw plus in other case
        this.drawText("+", horizontalPosition, verticalPositionOfSign, mPaint)

        //getting width of plus symbol
        mPaint.getTextWidths("+", arr)

        //moving horizontal position to the left of plus
        horizontalPosition += arr.sum()
    }

    //init absolute version of imaginary part of complex number, because minus is already drawn
    val absoluteFraction =
        Fraction(
            _upper = complexNumber.im.upper.absoluteValue,
            _lower = complexNumber.im.lower.absoluteValue
        )

    //draw imaginary part of number and get offset of middle of imaginary part
    val middlePositionOfImaginaryPart =
        this.drawFraction(
            absoluteFraction,
            horizontalPosition,
            y + if (!complexNumber.re.isInt() && complexNumber.im.isInt()) mPaint.getFractionLineVerticalOffset(
                complexNumber.re
            ) - high / 2
            else 0.0f,
            mPaint
        )

    //move horizontal position of drawing to the left of imaginary part
    horizontalPosition += mPaint.getFractionWidth(absoluteFraction)

    //calculate vertical position of right bracket and imaginary unit
    val imVerticalPosition = when
    {
        complexNumber.re.isInt() && complexNumber.im.isInt() ->
            y + middlePositionOfImaginaryPart - high / 2 - CanvasRenderSpecification.letterVerticalSpacing
        else ->
            y + +if (mPaint.getFractionLineVerticalOffset(complexNumber.im) >= mPaint.getFractionLineVerticalOffset(
                    complexNumber.re
                )
            ) middlePositionOfImaginaryPart
            else mPaint.getFractionLineVerticalOffset(complexNumber.re)
    }


    //draw right bracket and imaginary un it
    this.drawText("i)", horizontalPosition, imVerticalPosition, mPaint)

}

//draws complex number @complexNumber on canvas with pos [@x;@y]
fun Canvas.drawComplex(complexNumber: ComplexNumber, x: Float, Y: Float, mPaint: Paint)
{
    //error compansation for measuring letter height
    val y = Y - 5.0f
    val align = mPaint.textAlign
    mPaint.textAlign = Paint.Align.LEFT

    //work over some special situations
    when
    {
        //if it's real just draw fraction
        complexNumber.isReal() ->
        {
            drawFractions(complexNumber.re, x, y, mPaint)
        }
        //if it's imaginary then draw fraction and 'i' after it
        complexNumber.isImagination() ->
        {
            //size of fraction
            val imSize = mPaint.getFractionSize(complexNumber.im)

            //vertical offset for 'i' symbol
            val iVerticalOffset = (imSize.second - mPaint.getBaseHeight()) / 2


            drawFractions(complexNumber.im, x, y, mPaint)

            //draw 'i' symbol after fraction
            drawText(
                "i",
                x + imSize.first,
                y + iVerticalOffset + mPaint.getBaseHeight() - if (complexNumber.im.isInt()) 0.0f else mPaint.getVerticalSpacing(),
                mPaint
            )
        }
        else ->
        {
            /*
             * todo:: move from mutable list to mutable map, for prevention mistakes
             */

            //size of rendered number
            val complexNumberSize = mPaint.getComplexNumberSize(complexNumber)

            //list of size of elements in complex number
            val setSize = mutableListOf<Pair<Float, Float>>()

            //size of left bracket
            setSize.add(Pair(mPaint.measureText("("), mPaint.getBaseHeight()))

            //size of real part
            setSize.add(mPaint.getFractionSize(complexNumber.re))

            //if imaginary part is not containing minus sign than draw plus sign
            setSize.add(
                if (!complexNumber.im.isBeloweZero()) mPaint.getPlusSize()
                else Pair(
                    0.0f,
                    0.0f
                )
            )

            //size of imaginary part
            setSize.add(mPaint.getFractionSize(complexNumber.im))

            //size 'i' symbol
            setSize.add(Pair(mPaint.measureText("i"), mPaint.getBaseHeight()))

            //size of right btacket
            setSize.add(
                Pair(
                    mPaint.measureText(")"),
                    mPaint.getBaseHeight()
                )
            )

            //array of vertical offsets
            val verticalOffsetSet = Array(setSize.size) { 0.0f }

            //count vertical offset for each element in complex number
            for (element in setSize.indices)
            {

                verticalOffsetSet[element] =
                    (complexNumberSize.second - setSize[element].second) / 2

                //extra offset for fixing error of vertical positioning in reason of fractions and symbol height countinh
                when
                {
                    (element != 1 && element != 3) ->
                    {

                        if (!complexNumber.im.isInt() || !complexNumber.re.isInt())
                            verticalOffsetSet[element] -= 2 * mPaint.getVerticalSpacing() +
                                    if (element == 2 && !complexNumber.im.isBeloweZero()) 0.0f else mPaint.strokeWidth

                    }
                    (element == 1) ->
                    {
                        if (!complexNumber.im.isInt() && complexNumber.re.isInt())
                            verticalOffsetSet[element] -= 2 * mPaint.getVerticalSpacing() + mPaint.strokeWidth
                    }
                    (element == 3) ->
                    {
                        if (complexNumber.im.isInt() && !complexNumber.re.isInt())
                            verticalOffsetSet[element] -= 2 * mPaint.getVerticalSpacing() + mPaint.strokeWidth
                    }
                }
            }


            var horizontalOffset = 0.0f


            //draw left bracket
            drawText(
                "(",
                x + horizontalOffset,
                y + setSize[0].second + verticalOffsetSet[0],
                mPaint
            )

            horizontalOffset += setSize[0].first


            //draw real part
            drawFractions(complexNumber.re, x + horizontalOffset, y + verticalOffsetSet[1], mPaint)

            horizontalOffset += setSize[1].first + mPaint.getVerticalSpacing()

            //draw sign if it's not in im part
            if (!complexNumber.im.isBeloweZero())
            {
                drawPlus(x + horizontalOffset, y + verticalOffsetSet[2] + setSize[2].second, mPaint)

            }
            horizontalOffset += mPaint.getHorizontalSpacing()

            horizontalOffset += setSize[2].first

            //draw im part
            drawFractions(complexNumber.im, x + horizontalOffset, y + verticalOffsetSet[3], mPaint)

            horizontalOffset += setSize[3].first

            //draw 'i' symbol
            drawText(
                "i",
                x + horizontalOffset,
                y + verticalOffsetSet[4] + setSize[4].second,
                mPaint
            )

            horizontalOffset += setSize[4].first

            //draw right bracket
            drawText(
                ")",
                x + horizontalOffset,
                y + verticalOffsetSet[5] + setSize[5].second,
                mPaint
            )
        }
    }
    mPaint.textAlign = align
}
