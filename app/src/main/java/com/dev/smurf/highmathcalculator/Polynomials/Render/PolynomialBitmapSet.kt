package com.dev.smurf.highmathcalculator.Polynomials.Render

import android.graphics.Bitmap

//represent set of bitmap for polynomial image view holder
data class PolynomialBitmapSet(
    val leftPolynomialBitmap: Bitmap,
    val rightPolynomialBitmap: Bitmap,
    val resultPolynomialBitmap: Bitmap,
    val remainderPolynomialBitmap: Bitmap,
    val signBitmap: Bitmap
)
