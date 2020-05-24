package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.util.Log
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.PaintExtension.getFractionWidth
import kotlin.math.absoluteValue

//Extension for canvas to draw complex number
fun Canvas.drawComplex(complexNumber: ComplexNumber, x: Float, y: Float, mPaint: Paint) //: Float
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
            val iVerticalPos = y + this.drawFraction(complexNumber.im, x, y, mPaint) - high / 2 - CanvasRenderSpecification.letterVerticalSpacing

            val iHorizontalPos = x + mPaint.getFractionWidth(complexNumber.im)

            this.drawText("i",  iHorizontalPos, iVerticalPos, mPaint)

            return //iVerticalPos - high/2
        }
    }

    //counting length of left bracket
    val arr = FloatArray(1)
    mPaint.getTextWidths("(", arr)
    val lengthOfLeftBracket = arr.sum()

    //move horizontal position for drawing to the left of bracket
    var horizontalPosition = x + lengthOfLeftBracket

    //draw real part of number and get offset of middle of drawed number
    val middlePositionOfRealPart = this.drawFraction(complexNumber.re, horizontalPosition, y, mPaint)

    //move horizontal position to the left of real part
    horizontalPosition += mPaint.getFractionWidth(complexNumber.re)

    //calculate vertical position of left bracket and sign
    val verticalPositionOfSign = y + middlePositionOfRealPart - high / 2 - CanvasRenderSpecification.letterVerticalSpacing

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
        Fraction(_upper = complexNumber.im.upper.absoluteValue, _lower = complexNumber.im.lower.absoluteValue)

    //draw imaginary part of number and get offset of middle of imaginary part
    val middlePositionOfImaginaryPart = this.drawFraction(absoluteFraction, horizontalPosition, y, mPaint)

    //move horizontal position of drawing to the left of imaginary part
    horizontalPosition += mPaint.getFractionWidth(absoluteFraction)

    //calculate vertical position of right bracket and imaginary unit
    val imVerticalPosition = y + middlePositionOfImaginaryPart - high / 2 - CanvasRenderSpecification.letterVerticalSpacing

    //draw right bracket and imaginary un it
    this.drawText("i)", horizontalPosition, imVerticalPosition, mPaint)

}
