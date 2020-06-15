package com.dev.smurf.highmathcalculator.Matrix.Render

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.CanvasExtension.*
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.PaintExtension.*
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup


//class for rendering matrix
open class MatrixRenderInHolderStrategyConstracter
{

    companion object
    {
        //default bitmap cfg
        val bitmapConfig = Config.ARGB_8888

        //margin for start of bitmap
        const val horizontalMatrixMargin = 7.0f

        //margin for top of bitmap
        const val verticalMatrixMargin = 4.0f

        const val equalsSign = "="

        //render @sign on bitmap by @mPaint, @height is height of bitmap for creation
        fun renderSign(sign: String, height: Float, mPaint: Paint): Bitmap
        {
            //get sign size
            val rect = Rect()
            mPaint.getTextBounds(sign, 0, sign.length, rect)

            //for signs offset if margin
            val horizontalOffset =
                horizontalMatrixMargin

            //vertical offset is offset to render in middle of bitmap
            val verticalOffset = ((height - rect.height()) / 2) + rect.height()


            val bitmap = createBitmap(
                (rect.width() + (horizontalOffset * 2)).toInt(),
                height.toInt(),
                bitmapConfig
            )
            val canvas = Canvas(bitmap)
            canvas.drawText(sign, horizontalOffset, verticalOffset, mPaint)
            return bitmap
        }

        //return size of @sign by @mPaint
        fun getSignSize(sign: String, mPaint: Paint): Pair<Float, Float>
        {
            //get sign size
            val rect = Rect()
            mPaint.getTextBounds(sign, 0, sign.length, rect)

            return Pair(
                rect.width().toFloat(),
                rect.height().toFloat() + mPaint.fontMetrics.descent
            )
        }


        //brute force search of proper strategy
        fun getWorkStrategyGroup(
            matrixSet: MatrixGroup,
            maxWidth: Float,
            mPaint: Paint
        ): MatrixRenderStrategyGroup
        {
            var amountOfDots = 0
            val isLines = matrixSet.sign == MatrixGroup.DET
            val isVectors = matrixSet.sign == MatrixGroup.EIGENVECTOR
            while (!checkStrategyGroup(
                    constructStrategy(amountOfDots, isLines,isVectors),
                    matrixSet,
                    maxWidth,
                    mPaint
                )
                && (amountOfDots < 4)
            )
            {
                amountOfDots++
            }
            return constructStrategy(amountOfDots, isLines,isVectors)
        }

        //checks is sum of rendered matrix size is ok for veiw holder
        fun checkStrategyGroup(
            matrixRenderStrategyGroup: MatrixRenderStrategyGroup,
            matrixSet: MatrixGroup,
            maxWidth: Float,
            mPaint: Paint
        ) = (matrixRenderStrategyGroup.getMatrixSetSizeByStrategy(
            matrixSet,
            mPaint
        ).first <= maxWidth)

        //constracting strategy for render matrix group, @amountOdDots is amount of matrix that will be rendered as dots
        //@isLines is flag for render left matrix as determinant in lines
        fun constructStrategy(
            amountOfDots: Int,
            isLines: Boolean,
            isVectors: Boolean
        ): MatrixRenderStrategyGroup
        {
            val lftStrat =
                if (amountOfDots > 0) if (isLines) MatrixRenderStrategy.getMatrixInLinesAsDotsStrategy() else MatrixRenderStrategy.getMatrixInBracketsAsDotsStrategy()
                else if (isLines) MatrixRenderStrategy.getFullRenderStrategyInLines() else MatrixRenderStrategy.getFullRenderStrategyInBrackets()
            val rghtStrat =
                if (amountOfDots > 1) MatrixRenderStrategy.getMatrixInBracketsAsDotsStrategy() else MatrixRenderStrategy.getFullRenderStrategyInBrackets()

            val resStrat =
                if (isVectors)if (amountOfDots > 2) MatrixRenderStrategy.getMatrixInBracketsAsDotsStrategy() else MatrixRenderStrategy.getMatrixAsVectorsStrategy()
                else  if (amountOfDots > 2) MatrixRenderStrategy.getMatrixInBracketsAsDotsStrategy() else MatrixRenderStrategy.getFullRenderStrategyInBrackets()

            return MatrixRenderStrategyGroup(
                leftMatrixStrategy = lftStrat,
                rightMatrixStrategy = rghtStrat,
                resMatrixStrategy = resStrat
            )
        }


    }


}
