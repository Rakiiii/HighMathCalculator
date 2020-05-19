package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialRoots
import com.dev.smurf.highmathcalculator.Utils.getComplexNumberWidth

//draw polinom on canvas returns last point of polynomial render and vertical offset for render signs
fun Canvas.drawPolynomial(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint): Pair<Float, Float>
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
            if (fractionFlag) CanvasRenderSpecification.getLetterHigh(mPaint)-
                    CanvasRenderSpecification.letterVerticalSpacing -
                    CanvasRenderSpecification.getLineWidth(mPaint)/2
            else CanvasRenderSpecification.getLetterHigh(mPaint)/2

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

            val text = i.first + if (i == polynomialForRender.last() || (i.second.isBelowZero())) "" else "+"

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

//draw equation that setted by polynomial @polynomial on canvas
fun Canvas.drawEquation(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint)
{
    //draw polynomial
    val offsets = this.drawPolynomial(polynomial, x, y, mPaint)

    //draw equals zero after polynomial to get equation
    this.drawText("=0", offsets.first, offsets.second, mPaint)
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
