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

open class MatrixRender
{

    companion object
    {
        val bitmapConfig = Config.ARGB_8888
        val horizontalMatrixMargin = 7.0f
        val verticalMatrixMargin = 4.0f
        fun renderMatrixSet(matrixSet: MatrixGroup, maxWidth: Float, mPaint: Paint): BitmapSet
        {
            val matrixSizeSet = Array(4) { Pair(0.0f, 0.0f) }
            matrixSizeSet[0] =
                if (matrixSet.sign == MatrixGroup.DET) mPaint.getMatrixInLinesSize(
                    matrixSet.leftMatrix
                )
                else mPaint.getMatrixInBracketsSize(matrixSet.leftMatrix)
            matrixSizeSet[1] =
                if (!matrixSet.rightMatrix.isEmpty()) mPaint.getMatrixInBracketsSize(
                    matrixSet.rightMatrix
                )
                else Pair(0.0f, 0.0f)
            val textRect = Rect()
            mPaint.getTextBounds(matrixSet.sign, 0, matrixSet.sign.length, textRect)
            matrixSizeSet[2] = Pair(textRect.width().toFloat(), textRect.height().toFloat())
            matrixSizeSet[3] = mPaint.getMatrixInBracketsSize(matrixSet.resMatrix)

            var overallWidth = matrixSizeSet.sumByDouble { it.first.toDouble() }.toFloat()

            var result: BitmapSet? = null

            if (overallWidth > maxWidth)
            {
                //do something
                //todo:: checking different combinations of matrix render( dots etc...), and render possibla one
            }
            else
            {

                val maxHeigh =
                    (verticalMatrixMargin * 2) + matrixSizeSet.maxBy { it.second }!!.second

                val lftBitmap = renderWithSpecsAtPosition(
                    matrixSet.leftMatrix,
                    if (matrixSet.sign == MatrixGroup.DET) SpecsType.FullInLine else SpecsType.Full,
                    mPaint,
                    Pair(matrixSizeSet[0].first + (horizontalMatrixMargin * 2), maxHeigh)
                )

                val rhtBitmap = if (!matrixSet.rightMatrix.isEmpty()) renderWithSpecsAtPosition(
                    matrixSet.rightMatrix, SpecsType.Full, mPaint,
                    Pair(matrixSizeSet[1].first + (horizontalMatrixMargin * 2), maxHeigh)
                )
                else BitmapSet.getEmptyBitmap()

                val signumBitmap = renderSignum(matrixSet.sign, maxHeigh, mPaint)

                val resBitmap = renderWithSpecsAtPosition(
                    matrixSet.resMatrix,
                    if (matrixSet.resMatrix.isNumber()) SpecsType.Number else SpecsType.Full,
                    mPaint,
                    Pair(matrixSizeSet[3].first + (horizontalMatrixMargin * 2), maxHeigh)
                )

                result = BitmapSet(
                    leftMatrixBitmap = lftBitmap,
                    rightMatrixBitmap = rhtBitmap,
                    signumBitmap = signumBitmap,
                    equlsSignumBitmap = renderSignum("=",maxHeigh,mPaint),
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
            val bitmap = Bitmap.createBitmap(
                bitmapSize.first.toInt(),
                bitmapSize.second.toInt(),
                bitmapConfig
            )

            val canvas = Canvas(bitmap)

            val matrixSize = getSizeBySpec(matrix, spec, mPaint)
            val horizontalOffset = (bitmapSize.first - matrixSize.first) / 2
            val verticalOffset = (bitmapSize.second - matrixSize.second) / 2

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
            return when (spec)
            {
                SpecsType.Full -> return mPaint.getMatrixInBracketsSize(matrix)
                SpecsType.FullInLine -> return mPaint.getMatrixInLinesSize(matrix)
                SpecsType.Number -> return Pair(
                    mPaint.getComplexNumberWidth(matrix.matrices[0][0]),
                    mPaint.getComplexNumberHigh(matrix.matrices[0][0])
                )
                else -> Pair(0.0f, 0.0f)
            }
        }

        fun renderSignum(signum: String, height: Float, mPaint: Paint): Bitmap
        {
            val rect = Rect()
            mPaint.getTextBounds(signum, 0, signum.length, rect)
            val horizontalOffset = horizontalMatrixMargin
            val verticalOffset = ((height - rect.height()) / 2)+rect.height()
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


    data class MatrixRenderSpecification(
        var matrixSet: MatrixGroup,
        var renderSpecs: RenderSpecification
    )

    data class RenderSpecification(
        var leftMatrixSpecs: SpecsType,
        var rightMatrixSpecs: SpecsType,
        var signumMatrixSpecs: SpecsType,
        var equlsSignumMatrixSpec:SpecsType,
        var resultMatrixSpecs: SpecsType
    )

    enum class SpecsType
    {
        WithDots, Full, OnlyDots, WithDotsInLine, FullInLine, OnlyDotsInLine, Number,Empty
    }

    data class BitmapSet(
        var leftMatrixBitmap: Bitmap,
        var rightMatrixBitmap: Bitmap,
        var signumBitmap: Bitmap,
        var equlsSignumBitmap : Bitmap,
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