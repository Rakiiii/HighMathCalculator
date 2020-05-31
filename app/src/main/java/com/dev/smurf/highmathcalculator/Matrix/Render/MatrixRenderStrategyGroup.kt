package com.dev.smurf.highmathcalculator.Matrix.Render

import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup

//matrix strategy render set for matrix in group
class MatrixRenderStrategyGroup(
    private val leftMatrixStrategy: MatrixRenderStrategy,
    private val rightMatrixStrategy: MatrixRenderStrategy,
    private val resMatrixStrategy: MatrixRenderStrategy
)
{
    //render all matrix in group with setted startegy
    fun renderMatrix(matrixSet: MatrixGroup, mPaint: Paint): MatrixBitmapSet
    {
        //array for size of matrix
        val matrixSizeSet = Array(5) { Pair(0.0f, 0.0f) }

        matrixSizeSet[0] = leftMatrixStrategy.matrixSize(mPaint, matrixSet.leftMatrix)
        matrixSizeSet[1] = rightMatrixStrategy.matrixSize(mPaint, matrixSet.rightMatrix)

        //get sign size
        val textRect = Rect()
        mPaint.getTextBounds(matrixSet.sign, 0, matrixSet.sign.length, textRect)
        matrixSizeSet[2] = Pair(textRect.width().toFloat(), textRect.height().toFloat())

        matrixSizeSet[3] = resMatrixStrategy.matrixSize(mPaint, matrixSet.resMatrix)
        //get sign size
        mPaint.getTextBounds(MatrixRenderInHolderStrategyConstracter.equalsSign, 0, MatrixRenderInHolderStrategyConstracter.equalsSign.length, textRect)
        matrixSizeSet[4] = Pair(textRect.width().toFloat(), textRect.height().toFloat())


        //count max height of rendered for counting offset [must be rendered in same heigh of bitmaps]
        val maxHeight =
            (MatrixRenderInHolderStrategyConstracter.verticalMatrixMargin * 2) + matrixSizeSet.maxBy { it.second }!!.second

        //render left matrix
        val lftBitmap = leftMatrixStrategy.renderAtPosition(
            matrixSet.leftMatrix, mPaint, Pair(
                matrixSizeSet[0].first + (MatrixRenderInHolderStrategyConstracter.horizontalMatrixMargin * 2),
                maxHeight
            )
        )

        //render right matrix
        val rhtBitmap = rightMatrixStrategy.renderAtPosition(
            matrixSet.rightMatrix, mPaint, Pair(
                matrixSizeSet[1].first + (MatrixRenderInHolderStrategyConstracter.horizontalMatrixMargin * 2),
                maxHeight
            )
        )

        //render result matrix
        val resBitmap = resMatrixStrategy.renderAtPosition(
            matrixSet.resMatrix, mPaint, Pair(
                matrixSizeSet[3].first + (MatrixRenderInHolderStrategyConstracter.horizontalMatrixMargin * 2),
                maxHeight
            )
        )


        return MatrixBitmapSet(
            leftMatrixBitmap = lftBitmap,
            rightMatrixBitmap = rhtBitmap,
            signBitmap = MatrixRenderInHolderStrategyConstracter.renderSign(
                matrixSet.sign,
                maxHeight,
                mPaint
            ),
            equalsSignBitmap = MatrixRenderInHolderStrategyConstracter.renderSign(
                MatrixRenderInHolderStrategyConstracter.equalsSign,
                maxHeight,
                mPaint
            ),
            resultMatrixBitmap = resBitmap
        )
    }

    //count overall size of bitmaps for render
    fun getMatrixSetSizeByStrategy(matrixSet: MatrixGroup, mPaint: Paint): Pair<Float, Float>
    {
        val matrixSizeSet = Array(5) { Pair(0.0f, 0.0f) }

        matrixSizeSet[0] = leftMatrixStrategy.matrixSize(mPaint, matrixSet.leftMatrix)
        matrixSizeSet[1] = rightMatrixStrategy.matrixSize(mPaint, matrixSet.rightMatrix)
        matrixSizeSet[2] = resMatrixStrategy.matrixSize(mPaint, matrixSet.resMatrix)

        matrixSizeSet[3] = MatrixRenderInHolderStrategyConstracter.getSignSize(matrixSet.sign, mPaint)
        matrixSizeSet[4] = MatrixRenderInHolderStrategyConstracter.getSignSize(MatrixRenderInHolderStrategyConstracter.equalsSign, mPaint)

        return Pair(
            matrixSizeSet.sumByDouble { it.first.toDouble() }.toFloat(),
            (matrixSizeSet.maxBy { it.second } ?: Pair(0.0f, 0.0f)).second)
    }
}