package com.dev.smurf.highmathcalculator.ui.adapters.MatrixAdapters

import android.graphics.*
import android.graphics.Bitmap.Config
import android.graphics.Bitmap.createBitmap
import com.dev.smurf.highmathcalculator.CanvasExtension.drawComplex
import com.dev.smurf.highmathcalculator.CanvasExtension.drawMatrixInBrackets
import com.dev.smurf.highmathcalculator.CanvasExtension.drawMatrixInLines
import com.dev.smurf.highmathcalculator.Utils.getComplexNumberHigh
import com.dev.smurf.highmathcalculator.Utils.getComplexNumberWidth
import com.dev.smurf.highmathcalculator.Utils.getMatrixInBracketsSize
import com.dev.smurf.highmathcalculator.Utils.getMatrixInLinesSize
import com.example.smurf.mtarixcalc.Matrix
import com.example.smurf.mtarixcalc.MatrixGroup

//class for rendering matrix
open class MatrixRender
{

    companion object
    {
        //deff bitmap cfg
        val bitmapConfig = Config.ARGB_8888

        //margin for start of bitmap
        val horizontalMatrixMargin = 7.0f

        //margin for top of bitmap
        val verticalMatrixMargin = 4.0f

        val equlsSignum = "="

        //render matrix pojo in window with size maxWidth
        fun renderMatrixSet(matrixSet: MatrixGroup, maxWidth: Float, mPaint: Paint): BitmapSet
        {
            //array for size of matrix
            val matrixSizeSet = Array(5) { Pair(0.0f, 0.0f) }

            //get size of left matrix, it must be rendered in brackets always except determinant case
            matrixSizeSet[0] = getSizeBySpec(
                matrixSet.leftMatrix,
                if (matrixSet.sign == MatrixGroup.DET) SpecsType.FullInLine else SpecsType.Full,
                mPaint
            )

            //get size of right matrix, must be in brackets or empty
            matrixSizeSet[1] = getSizeBySpec(
                matrixSet.rightMatrix,
                if (matrixSet.rightMatrix.isEmpty()) SpecsType.Empty else SpecsType.Full,
                mPaint
            )

            //get signum size
            val textRect = Rect()
            mPaint.getTextBounds(matrixSet.sign, 0, matrixSet.sign.length, textRect)
            matrixSizeSet[2] = Pair(textRect.width().toFloat(), textRect.height().toFloat())

            //get result matrix size
            matrixSizeSet[3] = getSizeBySpec(
                matrixSet.resMatrix,
                if (matrixSet.resMatrix.isNumber()) SpecsType.Number else SpecsType.Full,
                mPaint
            )

            //get size of equls signum
            matrixSizeSet[4] = Pair(textRect.width().toFloat(), textRect.height().toFloat())

            //count overall width
            var overallWidth = matrixSizeSet.sumByDouble { it.first.toDouble() }.toFloat()

            //init return value
            var result: BitmapSet? = null

            //if ovwerall width greater then window width then render in special way
            if (overallWidth > maxWidth)
            {
                //do something
                //todo:: checking different combinations of matrix render( dots etc...), and render possibla one
            }
            //if width is ok, render full matrixs in line
            else
            {
                //count max height of rendered for counting offset [must be rendered in same heigh of bitmaps]
                val maxHeight =
                    (verticalMatrixMargin * 2) + matrixSizeSet.maxBy { it.second }!!.second

                //render left matrix
                val lftBitmap = renderWithSpecsAtPosition(
                    matrixSet.leftMatrix,
                    if (matrixSet.sign == MatrixGroup.DET) SpecsType.FullInLine else SpecsType.Full,
                    mPaint,
                    Pair(matrixSizeSet[0].first + (horizontalMatrixMargin * 2), maxHeight)
                )

                //render right matrix
                val rhtBitmap = renderWithSpecsAtPosition(
                    matrixSet.rightMatrix,
                    if (!matrixSet.rightMatrix.isEmpty()) SpecsType.Full else SpecsType.Empty,
                    mPaint,
                    Pair(matrixSizeSet[1].first + (horizontalMatrixMargin * 2), maxHeight)
                )

                //render signum
                val signumBitmap = renderSignum(matrixSet.sign, maxHeight, mPaint)

                //render result matrix
                val resBitmap = renderWithSpecsAtPosition(
                    matrixSet.resMatrix,
                    if (matrixSet.resMatrix.isNumber()) SpecsType.Number else SpecsType.Full,
                    mPaint,
                    Pair(matrixSizeSet[3].first + (horizontalMatrixMargin * 2), maxHeight)
                )

                result = BitmapSet(
                    leftMatrixBitmap = lftBitmap,
                    rightMatrixBitmap = rhtBitmap,
                    signumBitmap = signumBitmap,
                    equlsSignumBitmap = renderSignum(equlsSignum, maxHeight, mPaint),
                    resultMatrixBitmap = resBitmap
                )
            }

            return result ?: BitmapSet.getEmptyBitmapSet()
        }

        fun renderWithSpecsAtPosition(
            matrix: Matrix,
            spec: SpecsType,
            mPaint: Paint,
            bitmapSize: Pair<Float, Float>,
            x: Float = 0.0f,
            y: Float = 0.0f
        ): Bitmap
        {
            //if empty matrix return empty map
            if (spec == SpecsType.Empty) return BitmapSet.getEmptyBitmap()

            //init bitmap
            val bitmap = Bitmap.createBitmap(
                bitmapSize.first.toInt(),
                bitmapSize.second.toInt(),
                bitmapConfig
            )

            //init canvas
            val canvas = Canvas(bitmap)

            //count matrix size
            val matrixSize = getSizeBySpec(matrix, spec, mPaint)

            //count horizontal offset,rendet matrix in the middle of bitmap
            val horizontalOffset = (bitmapSize.first - matrixSize.first) / 2

            //count vertical offset, if matrix smaller then heighest then it must be rendered in the middle
            val verticalOffset = (bitmapSize.second - matrixSize.second) / 2

            //select render method by spec type
            when (spec)
            {
                SpecsType.Full -> canvas.drawMatrixInBrackets(
                    matrix,
                    x + horizontalOffset,
                    y + verticalOffset,
                    mPaint
                )
                SpecsType.FullInLine -> canvas.drawMatrixInLines(
                    matrix,
                    x + horizontalOffset,
                    y + verticalOffset,
                    mPaint
                )
                SpecsType.Number -> canvas.drawComplex(
                    matrix.matrices[0][0],
                    x + horizontalOffset,
                    y + verticalOffset,
                    mPaint
                )
            }

            return bitmap
        }

        fun getSizeBySpec(
            matrix: Matrix,
            spec: SpecsType,
            mPaint: Paint
        ): Pair<Float, Float>
        {
            //select type of render for size count by spec type
            return when (spec)
            {
                SpecsType.Full -> return mPaint.getMatrixInBracketsSize(matrix)
                SpecsType.FullInLine -> return mPaint.getMatrixInLinesSize(matrix)
                SpecsType.Number -> return Pair(
                    mPaint.getComplexNumberWidth(matrix.matrices[0][0]),
                    mPaint.getComplexNumberHigh(matrix.matrices[0][0])
                )
                SpecsType.Empty -> Pair(0.0f, 0.0f)
                else -> Pair(0.0f, 0.0f)
            }
        }

        fun renderSignum(signum: String, height: Float, mPaint: Paint): Bitmap
        {
            //get signum size
            val rect = Rect()
            mPaint.getTextBounds(signum, 0, signum.length, rect)

            //for signums offset if margin
            val horizontalOffset = horizontalMatrixMargin

            //vertical offset is offset to render in middle of bitmap
            val verticalOffset = ((height - rect.height()) / 2) + rect.height()


            val bitmap = Bitmap.createBitmap(
                (rect.width() + (horizontalOffset * 2)).toInt(),
                height.toInt(),
                bitmapConfig
            )
            val canvas = Canvas(bitmap)
            canvas.drawText(signum, horizontalOffset, verticalOffset, mPaint)
            return bitmap
        }
    }



    //type of render matrix
    enum class SpecsType
    {
        WithDots, Full, OnlyDots, WithDotsInLine, FullInLine, OnlyDotsInLine, Number, Empty,MultyLine
    }

    //represent set of bitmap
    data class BitmapSet(
        var leftMatrixBitmap: Bitmap,
        var rightMatrixBitmap: Bitmap,
        var signumBitmap: Bitmap,
        var equlsSignumBitmap: Bitmap,
        var resultMatrixBitmap: Bitmap
    )
    {
        companion object
        {
            fun getEmptyBitmapSet() = BitmapSet(
                getEmptyBitmap(),
                getEmptyBitmap(),
                getEmptyBitmap(),
                getEmptyBitmap(),
                getEmptyBitmap()
            )

            fun getEmptyBitmap() = createBitmap(1, 1, bitmapConfig)

        }
    }
}