package com.dev.smurf.highmathcalculator.Polynomials.Render

import android.graphics.Paint
import com.dev.smurf.highmathcalculator.CanvasExtension.drawMultiLinePolynomial
import com.dev.smurf.highmathcalculator.PaintExtension.*
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase

//set methods for constricting strategy for polynomial render
class RenderStrategyConstracter
{
    companion object
    {
        //constructs separation for polynomial on multy line
        fun constructRangeArrayForPolynomial(
            polynomial: PolynomialBase,
            maxWidth: Float,
            mPaint: Paint
        ): Array<IntRange>
        {
            val rangeList = MutableList<IntRange>(0) { 0..0 }
            val size = polynomial.renderFormat().size
            var start = 0

            //imperatively constructing new line, while polynomial is not end
            do
            {
                val subRange =
                    constructPolynomialMaxRangeByStart(polynomial, maxWidth, start, mPaint)
                rangeList.add(subRange)
                start = subRange.last + 1
            } while (subRange.last != size - 1)

            return Array(rangeList.size) { pos -> rangeList[pos] }
        }


        //constructs line with max amount of cofs with max width @maxWidth
        //cheking all cofs from position @start
        fun constructPolynomialMaxRangeByStart(
            polynomial: PolynomialBase,
            maxWidth: Float,
            start: Int,
            mPaint: Paint
        ): IntRange
        {
            val size = polynomial.renderFormat().size
            var prevBestRange = start
            //we must get in mind that possibly we will render some cofs before or after line of cofs,
            val offset = 3 * mPaint.getPlusWidth() + 4 * mPaint.getHorizontalSpacing()
            //checks while line is too big
            for (i in start until size)
            {
                val renderSize = mPaint.getPolynomialRangeSize(polynomial, start..i)
                if (renderSize.first + offset >= maxWidth) return (start..prevBestRange)
                else
                {
                    prevBestRange = i
                }
            }
            return start..prevBestRange
        }

        fun constructRenderStrategy(
            polynomial: PolynomialBase,
            maxWidth: Float,
            maxHeight: Float,
            mPaint: Paint
        ): PolynomialRenderInHolderStrategy
        {
            //constructing strategy it self
            val size = mPaint.getPolynomialSize(polynomial)
            //if we do not need any magic, then just draw polynomial
            if (size.first < maxWidth && size.second < maxHeight) return PolynomialRenderInHolderStrategy.createDefaultStrategy()

            //else try to separate it on many lines
            val ranges = constructRangeArrayForPolynomial(polynomial, maxWidth, mPaint)

            val fixedSize = mPaint.getMultiLinePolynomialSize(polynomial, ranges)

            //if it's to height , then lets draw dots not polynomial
            return if (fixedSize.second < maxHeight) PolynomialRenderInHolderStrategy(
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

}