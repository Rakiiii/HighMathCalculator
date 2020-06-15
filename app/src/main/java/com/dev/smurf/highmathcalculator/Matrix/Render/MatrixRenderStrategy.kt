package com.dev.smurf.highmathcalculator.Matrix.Render

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.dev.smurf.highmathcalculator.CanvasExtension.*
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.PaintExtension.*

//implementation of strategy pattern for matrix render
class MatrixRenderStrategy(
    //way of matrix render
    val renderMatrix: Canvas.(Matrix, Float, Float, Paint) -> Unit,
    //size of rendered matrix
    val matrixSize: Paint.(Matrix) -> Pair<Float, Float>
)
{
    //render matrix @matrix with setted strategy on new bitmap with size @bitmapSize by paint @mPaint,
    //left angle is on cord [@x,@y]
    fun renderAtPosition(
        matrix: Matrix,
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
            MatrixRenderInHolderStrategyConstracter.bitmapConfig
        )

        //init canvas
        val canvas = Canvas(bitmap)

        //count matrix size
        val size = matrixSize(mPaint, matrix)

        //count horizontal offset,rendet matrix in the middle of bitmap
        val horizontalOffset = (bitmapSize.first - size.first) / 2

        //count vertical offset, if matrix smaller then heighest then it must be rendered in the middle
        val verticalOffset = (bitmapSize.second - size.second) / 2

        //render matrix it self
        renderMatrix(
            canvas, matrix,
            x + horizontalOffset,
            y + verticalOffset,
            mPaint
        )

        return bitmap
    }

    companion object
    {
        //return strategy to render full matrix in brackets
        fun getFullRenderStrategyInBrackets() =
            MatrixRenderStrategy(renderMatrix = { matrix, fl, fl2, paint ->
                this.drawMatrixInBrackets(
                    matrix,
                    fl,
                    fl2,
                    paint
                )
            }, matrixSize = { matrix -> this.getMatrixInBracketsSize(matrix) })

        //return strategy to render full matrix in lines as determinant
        fun getFullRenderStrategyInLines() =
            MatrixRenderStrategy(renderMatrix = { matrix, fl, fl2, paint ->
                this.drawMatrixInLines(
                    matrix,
                    fl,
                    fl2,
                    paint
                )
            }, matrixSize = { matrix -> this.getMatrixInLinesSize(matrix) })

        //return dots in brackets matrix render startegy
        fun getMatrixInBracketsAsDotsStrategy() =
            MatrixRenderStrategy(renderMatrix = { matrix, fl, fl2, paint ->
                this.drawMatrixInBracketsAsDots(
                    matrix,
                    fl,
                    fl2,
                    paint
                )
            }, matrixSize = { matrix -> this.getMatrixInBracketsAsDotsSize(matrix) })

        //return dots inlines matrix render startegy
        fun getMatrixInLinesAsDotsStrategy() =
            MatrixRenderStrategy(renderMatrix = { matrix, fl, fl2, paint ->
                this.drawMatrixInLinesAsDots(
                    matrix,
                    fl,
                    fl2,
                    paint
                )
            }, matrixSize = { matrix -> this.getMatrixInLinesAsDotsSize(matrix) })

        fun getMatrixAsVectorsStrategy() =
            MatrixRenderStrategy(renderMatrix = { matrix, fl, fl2, paint ->
                this.drawMatrixAsSetOfVectors(
                    matrix,
                    fl,
                    fl2,
                    paint
                )
            }, matrixSize = { matrix -> this.getMatrixSizeAsVectors(matrix) })
    }
}
