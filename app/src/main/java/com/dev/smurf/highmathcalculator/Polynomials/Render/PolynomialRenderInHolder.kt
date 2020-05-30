package com.dev.smurf.highmathcalculator.Polynomials.Render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.dev.smurf.highmathcalculator.CanvasExtension.drawMultiLinePolynomial
import com.dev.smurf.highmathcalculator.CanvasExtension.drawPolynomial
import com.dev.smurf.highmathcalculator.CanvasExtension.drawPolynomialAsDots
import com.dev.smurf.highmathcalculator.Matrix.Render.MatrixRender
import com.dev.smurf.highmathcalculator.PaintExtension.*
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.example.smurf.mtarixcalc.PolynomialGroup

class PolynomialRenderInHolder
{
    companion object
    {

        //default bitmap cfg
        val bitmapConfig = Bitmap.Config.ARGB_8888

        fun renderWithStrategy(
            polynomialGroup: PolynomialGroup,
            maxWidth: Float,
            maxHeight: Float,
            mPaint: Paint
        ): PolynomialBitmapSet
        {
            val signSize = getSignSize(polynomialGroup.polSignPolynomial, mPaint)
            val finalWidth = maxWidth - (signSize.first + mPaint.getHorizontalSpacing())

            val leftStrategy = RenderStrategyConstracter.constructRenderStrategy(
                polynomial = polynomialGroup.polLeftPolynomial,
                maxWidth = finalWidth,
                maxHeight = maxHeight,
                mPaint = mPaint
            )

            val leftPolynomialBitmap = leftStrategy.renderAtMiddleOnNewBitmapMinSize(
                polynomial = polynomialGroup.polLeftPolynomial,
                mPaint = mPaint
            )

            val rightStrategy = RenderStrategyConstracter.constructRenderStrategy(
                polynomial = polynomialGroup.polRightPolynomial,
                maxWidth = finalWidth,
                maxHeight = maxHeight,
                mPaint = mPaint
            )

            val rightPolynomialBitmap = rightStrategy.renderAtMiddleOnNewBitmapMinSize(
                polynomial = polynomialGroup.polRightPolynomial,
                mPaint = mPaint
            )

            val resultPolynomialBitmap = RenderStrategyConstracter.constructRenderStrategy(
                polynomial = polynomialGroup.polResPolynomial,
                maxWidth = finalWidth,
                maxHeight = maxHeight,
                mPaint = mPaint
            ).renderAtMiddleOnNewBitmapMinSize(
                polynomial = polynomialGroup.polResPolynomial,
                mPaint = mPaint
            )

            val remainderPolynomialBitmap =RenderStrategyConstracter.constructRenderStrategy(
                polynomial = polynomialGroup.polOstPolynomial,
                maxWidth = finalWidth,
                maxHeight = maxHeight,
                mPaint = mPaint
            ).renderAtMiddleOnNewBitmapMinSize(
                polynomial = polynomialGroup.polOstPolynomial,
                mPaint = mPaint
            )

            val maxLineWidth = if (leftStrategy.getSize(
                    mPaint, polynomialGroup.polLeftPolynomial
                ).first > rightStrategy.getSize(
                    mPaint, polynomialGroup.polRightPolynomial
                ).first
            ) leftStrategy.getSize(mPaint, polynomialGroup.polLeftPolynomial).first
            else rightStrategy.getSize(mPaint, polynomialGroup.polRightPolynomial).first

            val signBitmap = renderSign(polynomialGroup.polSignPolynomial, maxLineWidth, mPaint)

            return PolynomialBitmapSet(
                leftPolynomialBitmap = leftPolynomialBitmap,
                rightPolynomialBitmap = rightPolynomialBitmap,
                signBitmap = signBitmap,
                resultPolynomialBitmap = resultPolynomialBitmap,
                remainderPolynomialBitmap = remainderPolynomialBitmap
            )
        }

        //render @sign on bitmap by @mPaint, @height is height of bitmap for creation
        fun renderSign(sign: String, width: Float, mPaint: Paint): Bitmap
        {
            val fs = mPaint.textSize
            mPaint.textSize = fs * 2

            //get sign size
            val rect = Rect()
            mPaint.getTextBounds(sign, 0, sign.length, rect)

            val bitmap = Bitmap.createBitmap(
                width.toInt() + rect.width(),
                (mPaint.getBaseHeight() + mPaint.getVerticalSpacing()).toInt(),
                bitmapConfig
            )

            val canvas = Canvas(bitmap)

            canvas.drawText(
                sign,
                0,
                sign.length,
                0.0f,
                (mPaint.getBaseHeight() / 2) + rect.height(),
                mPaint
            )
            canvas.drawLine(
                3 * mPaint.measureText(sign),
                mPaint.getBaseHeight() / 2,
                width + rect.width().toFloat(),
                mPaint.getBaseHeight() / 2,
                mPaint
            )

            mPaint.textSize = fs

            return bitmap
        }



        //return size of @sign by @mPaint
        fun getSignSize(sign: String, mPaint: Paint): Pair<Float, Float>
        {
            val fs = mPaint.textSize
            mPaint.textSize = fs * 2

            //get sign size
            val rect = Rect()
            mPaint.getTextBounds(sign, 0, sign.length, rect)

            mPaint.textSize = fs

            return Pair(rect.width().toFloat(), rect.height().toFloat())
        }

    }



}