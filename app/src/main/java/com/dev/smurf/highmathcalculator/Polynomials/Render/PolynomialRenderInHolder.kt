package com.dev.smurf.highmathcalculator.Polynomials.Render

import android.graphics.Canvas
import android.graphics.Paint
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase

class PolynomialRenderInHolder
{
    companion object
    {

    }

    class PolynomialRenderStrategy(
        private val renderPolynomial: Canvas.(PolynomialBase, Float, Float, Paint) -> Unit,
        private val getSize: Paint.(PolynomialBase)->Pair<Float,Float>)
}