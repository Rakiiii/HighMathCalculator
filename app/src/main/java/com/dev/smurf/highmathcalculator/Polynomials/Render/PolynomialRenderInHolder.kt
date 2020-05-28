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

            val leftStrategy = constructRenderStrategy(
                polynomial = polynomialGroup.polLeftPolynomial,
                maxWidth = finalWidth,
                maxHeight = maxHeight,
                mPaint = mPaint
            )

            Log.d(
                "size@",
                "finWidth:" + finalWidth.toString() + " pol width:" + leftStrategy.getSize(
                    mPaint,
                    polynomialGroup.polLeftPolynomial
                ).first.toString()
            )

            val leftPolynomialBitmap = leftStrategy.renderAtMiddleOnNewBitmapMinSize(
                polynomial = polynomialGroup.polLeftPolynomial,
                mPaint = mPaint
            )

            val rightStrategy = constructRenderStrategy(
                polynomial = polynomialGroup.polRightPolynomial,
                maxWidth = finalWidth,
                maxHeight = maxHeight,
                mPaint = mPaint
            )

            val rightPolynomialBitmap = rightStrategy.renderAtMiddleOnNewBitmapMinSize(
                polynomial = polynomialGroup.polRightPolynomial,
                mPaint = mPaint
            )

            val resultPolynomialBitmap = constructRenderStrategy(
                polynomial = polynomialGroup.polResPolynomial,
                maxWidth = finalWidth,
                maxHeight = maxHeight,
                mPaint = mPaint
            ).renderAtMiddleOnNewBitmapMinSize(
                polynomial = polynomialGroup.polResPolynomial,
                mPaint = mPaint
            )

            val remainderPolynomialBitmap = constructRenderStrategy(
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

        fun constructRangeArrayForPolynomial(
            polynomial: PolynomialBase,
            maxWidth: Float,
            mPaint: Paint
        ): Array<IntRange>
        {
            val rangeList = MutableList<IntRange>(0) { 0..0 }
            val size = polynomial.renderFormat().size
            var start = 0

            do
            {
                val subRange =
                    constructPolynomialMaxRangeByStart(polynomial, maxWidth, start, mPaint)
                rangeList.add(subRange)
                start = subRange.last + 1
            } while (subRange.last != size - 1)

            return Array(rangeList.size) { pos -> rangeList[pos] }
        }

        fun constructPolynomialMaxRangeByStart(
            polynomial: PolynomialBase,
            maxWidth: Float,
            start: Int,
            mPaint: Paint
        ): IntRange
        {
            val size = polynomial.renderFormat().size
            var prevBestRange = start
            val offset = 3*mPaint.getPlusWidth() + 4*mPaint.getHorizontalSpacing()
            for (i in start until size)
            {
                val renderSize = mPaint.getPolynomialRangeSize(polynomial, start..i)
                if (renderSize.first+offset >= maxWidth) return (start..prevBestRange)
                else
                {
                    prevBestRange = i
                }
            }
            return start..prevBestRange
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

        fun constructRenderStrategy(
            polynomial: PolynomialBase,
            maxWidth: Float,
            maxHeight: Float,
            mPaint: Paint
        ): PolynomialRenderInHolderStrategy
        {
            val size = mPaint.getPolynomialSize(polynomial)
            if (size.first < maxWidth && size.second < maxHeight) return PolynomialRenderInHolderStrategy.createDefaultStrategy()

            val ranges = constructRangeArrayForPolynomial(polynomial, maxWidth, mPaint)

            val fixedSize = mPaint.getMultiLinePolynomialSize(polynomial, ranges)

            return if (/*fixedSize.first < maxWidth &&*/ fixedSize.second < maxHeight) PolynomialRenderInHolderStrategy(
                renderPolynomial = { pol, x, y, p ->
                    this.drawMultiLinePolynomial(
                        pol,
                        x,
                        y,
                        p,
                        ranges
                    )
                }, getSize = { pol -> this.getMultiLinePolynomialSize(pol, ranges) })
            else PolynomialRenderInHolderStrategy.createDotsStrategy()
        }

    }

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
                bitmapConfig
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
                bitmapConfig
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

    data class PolynomialBitmapSet(
        val leftPolynomialBitmap: Bitmap,
        val rightPolynomialBitmap: Bitmap,
        val resultPolynomialBitmap: Bitmap,
        val remainderPolynomialBitmap: Bitmap,
        val signBitmap: Bitmap
    )

}