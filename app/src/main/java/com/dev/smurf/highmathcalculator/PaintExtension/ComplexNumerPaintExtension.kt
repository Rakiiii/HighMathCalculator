package com.dev.smurf.highmathcalculator.PaintExtension

import android.graphics.Paint
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber

//return width of complex number
fun Paint.getComplexNumberWidth(complexNumber: ComplexNumber): Float
{
    var overallLength = 0.0f

    when
    {
        //if complex number is purely real
        complexNumber.isReal() ->
        {
            //it will draw only real part
            return this.getFractionWidth(complexNumber.re)
        }

        //if complex number is purely imaginary
        complexNumber.isImagination() ->
        {
            //its length composes length of i and length of imaginary part
            //get i length
            val arr = FloatArray(1)
            this.getTextWidths("i", arr)
            overallLength += arr.sum()

            //get imaginary part length
            overallLength += this.getFractionWidth(complexNumber.im)

            return overallLength

        }
    }

    //if complex number is full
    val arr = FloatArray(4)

    //if imaginary part is below zero
    if (complexNumber.im.isBeloweZero())
    {
        //count length of symbol with minus
        this.getTextWidths("(-i)", arr)
        overallLength += arr.sum()
    }
    else
    {
        //count length of sybol with plus in other case
        this.getTextWidths("(+i)", arr)
        overallLength += arr.sum()
    }

    //count length of real part
    overallLength += this.getFractionWidth(complexNumber.re)

    //count length of imaginary part
    overallLength += this.getFractionWidth(complexNumber.im)

    return overallLength

}

//return complex number height
fun Paint.getComplexNumberHeight(complexNumber: ComplexNumber): Float
{
    //if complex number isn't contains fraction, then it's height is height of symbols
    if (complexNumber.re.isInt() && complexNumber.im.isInt())
    {
        return CanvasRenderSpecification.getLetterHigh(this)
    }
    else
    {
        //if contains fraction then its fraction height
        return 2 * CanvasRenderSpecification.getLetterHigh(this) +
                2 * CanvasRenderSpecification.letterVerticalSpacing +
                CanvasRenderSpecification.getLineWidth(
                    this
                )
    }
}

//wrapper for hole size
fun Paint.getComplexNumberSize(complexNumber: ComplexNumber) =
    Pair(getComplexNumberWidth(complexNumber), getComplexNumberHeight(complexNumber))
