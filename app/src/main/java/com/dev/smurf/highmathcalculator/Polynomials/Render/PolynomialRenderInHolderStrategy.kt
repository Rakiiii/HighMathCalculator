package com.dev.smurf.highmathcalculator.Polynomials.Render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.dev.smurf.highmathcalculator.CanvasExtension.drawPolynomial
import com.dev.smurf.highmathcalculator.CanvasExtension.drawPolynomialAsDots
import com.dev.smurf.highmathcalculator.PaintExtension.getPolynomialSize
import com.dev.smurf.highmathcalculator.PaintExtension.getPolynomialSizeAsDots
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase

class PolynomialRenderInHolderStrategy(
    private val renderPolynomial: Canvas.(PolynomialBase, Float, Float, Paint) -> Unit,
    val getSize: Paint.(PolynomialBase) -> Pair<Float, Float>
)
{
    companion object
    {
        fun createDefaultStrategy() =
            PolynomialRenderInHolderStrategy(
                renderPolynomial = { pol, x, y, p ->
                    this.drawPolynomial(
                        pol,
                        x,
                        y,
                        p
                    )
                },
                getSize = { pol -> this.getPolynomialSize(pol) }
            )

        fun createDotsStrategy() = PolynomialRenderInHolderStrategy(
            renderPolynomial = { polynomialBase, x, y, paint ->
                this.drawPolynomialAsDots(
                    polynomialBase,
                    x,
                    y,
                    paint
                )
            }, getSize = { polynomialBase -> this.getPolynomialSizeAsDots(polynomialBase) }
        )
    }

    fun renderAtMiddleOnNewBitmapMinSize(
        polynomial: PolynomialBase,
        mPaint: Paint
    ): Bitmap
    {
        return renderAtMiddleOnNewBitmap(polynomial, mPaint, getSize(mPaint, polynomial))
    }


    fun renderAtMiddleOnNewBitmap(
        polynomial: PolynomialBase,
        mPaint: Paint,
        bitmapSize: Pair<Float, Float>
    ): Bitmap
    {
        val size = getSize(mPaint, polynomial)
        val verticalOffset = (bitmapSize.first - size.first) / 2
        val horizontalOffset = (bitmapSize.second - size.second) / 2

        if (size.first == 0.0f || size.second == 0.0f) return Bitmap.createBitmap(
            1, 1,
            PolynomialRenderInHolder.bitmapConfig
        )
        return renderAtPositionOnNewBitmap(
            polynomial,
            mPaint,
            bitmapSize,
            horizontalOffset,
            verticalOffset
        )
    }

    fun renderAtPositionOnNewBitmap(
        polynomial: PolynomialBase,
        mPaint: Paint,
        bitmapSize: Pair<Float, Float>,
        x: Float = 0.0f,
        y: Float = 0.0f
    ): Bitmap
    {
        //init bitmap
        val bitmap = Bitmap.createBitmap(
            bitmapSize.first.toInt(),
            bitmapSize.second.toInt(),
            PolynomialRenderInHolder.bitmapConfig
        )

        //init canvas
        val canvas = Canvas(bitmap)

        renderAtPosition(polynomial, mPaint, canvas, x, y)

        return bitmap
    }

    fun renderAtPosition(
        polynomial: PolynomialBase,
        mPaint: Paint,
        canvas: Canvas,
        x: Float = 0.0f,
        y: Float = 0.0f
    )
    {
        renderPolynomial(canvas, polynomial, x, y, mPaint)
    }
}
