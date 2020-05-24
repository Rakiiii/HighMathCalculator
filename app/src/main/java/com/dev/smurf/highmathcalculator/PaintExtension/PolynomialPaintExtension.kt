package com.dev.smurf.highmathcalculator.PaintExtension

import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase


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
