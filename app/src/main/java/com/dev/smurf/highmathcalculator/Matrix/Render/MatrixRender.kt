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
open class MatrixRender
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

            return Pair(rect.width().toFloat(), rect.height().toFloat())
        }

        //render matrix group with proper strategy
        fun renderWithStrategy(matrixSet: MatrixGroup, maxWidth: Float, mPaint: Paint) =
            getWorkStrategyGroup(matrixSet, maxWidth, mPaint).renderMatrix(matrixSet, mPaint)

        //brute force search of proper strategy
        private fun getWorkStrategyGroup(
            matrixSet: MatrixGroup,
            maxWidth: Float,
            mPaint: Paint
        ): MatrixRenderStrategyGroup
        {
            var amountOfDots = 0
            val isLines = matrixSet.sign == MatrixGroup.DET
            while (!checkStrategyGroup(
                    constructStrategy(amountOfDots, isLines),
                    matrixSet,
                    maxWidth,
                    mPaint
                )
                && (amountOfDots < 4)
            )
            {
                amountOfDots++
            }
            return constructStrategy(amountOfDots, isLines)
        }

        //checks is sum of rendered matrix size is ok for veiw holder
        private fun checkStrategyGroup(
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
        private fun constructStrategy(
            amountOfDots: Int,
            isLines: Boolean
        ): MatrixRenderStrategyGroup
        {
            val lftStrat =
                if (amountOfDots > 0) if (isLines) MatrixRenderStrategy.getMatrixInLinesAsDotsStrategy() else MatrixRenderStrategy.getMatrixInBracketsAsDotsStrategy()
                else if (isLines) MatrixRenderStrategy.getFullRenderStrategyInLines() else MatrixRenderStrategy.getFullRenderStrategyInBrackets()
            val rghtStrat =
                if (amountOfDots > 1) MatrixRenderStrategy.getMatrixInBracketsAsDotsStrategy() else MatrixRenderStrategy.getFullRenderStrategyInBrackets()

            val resStrat =
                if (amountOfDots > 2) MatrixRenderStrategy.getMatrixInBracketsAsDotsStrategy() else MatrixRenderStrategy.getFullRenderStrategyInBrackets()

            return MatrixRenderStrategyGroup(
                leftMatrixStrategy = lftStrat,
                rightMatrixStrategy = rghtStrat,
                resMatrixStrategy = resStrat
            )
        }


    }

    //represent set of bitmap
    data class BitmapSet(
        var leftMatrixBitmap: Bitmap,
        var rightMatrixBitmap: Bitmap,
        var signBitmap: Bitmap,
        var equalsSignBitmap: Bitmap,
        var resultMatrixBitmap: Bitmap
    )
    {
        companion object
        {
            fun getEmptyBitmapSet() =
                BitmapSet(
                    getEmptyBitmap(),
                    getEmptyBitmap(),
                    getEmptyBitmap(),
                    getEmptyBitmap(),
                    getEmptyBitmap()
                )

            fun getEmptyBitmap() = createBitmap(
                1, 1,
                bitmapConfig
            )

        }
    }

    //implementation of strategy pattern for matrix render
    data class MatrixRenderStrategy(
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
            val bitmap = createBitmap(
                bitmapSize.first.toInt(),
                bitmapSize.second.toInt(),
                bitmapConfig
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
        }
    }

    //matrix strategy render set for matrix in group
    class MatrixRenderStrategyGroup(
        private val leftMatrixStrategy: MatrixRenderStrategy,
        private val rightMatrixStrategy: MatrixRenderStrategy,
        private val resMatrixStrategy: MatrixRenderStrategy
    )
    {
        //render all matrix in group with setted startegy
        fun renderMatrix(matrixSet: MatrixGroup, mPaint: Paint): BitmapSet
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
            mPaint.getTextBounds(equalsSign, 0, equalsSign.length, textRect)
            matrixSizeSet[4] = Pair(textRect.width().toFloat(), textRect.height().toFloat())


            //count max height of rendered for counting offset [must be rendered in same heigh of bitmaps]
            val maxHeight =
                (verticalMatrixMargin * 2) + matrixSizeSet.maxBy { it.second }!!.second

            //render left matrix
            val lftBitmap = leftMatrixStrategy.renderAtPosition(
                matrixSet.leftMatrix, mPaint, Pair(
                    matrixSizeSet[0].first + (horizontalMatrixMargin * 2),
                    maxHeight
                )
            )

            //render right matrix
            val rhtBitmap = rightMatrixStrategy.renderAtPosition(
                matrixSet.rightMatrix, mPaint, Pair(
                    matrixSizeSet[1].first + (horizontalMatrixMargin * 2),
                    maxHeight
                )
            )

            //render result matrix
            val resBitmap = resMatrixStrategy.renderAtPosition(
                matrixSet.resMatrix, mPaint, Pair(
                    matrixSizeSet[3].first + (horizontalMatrixMargin * 2),
                    maxHeight
                )
            )


            return BitmapSet(
                leftMatrixBitmap = lftBitmap,
                rightMatrixBitmap = rhtBitmap,
                signBitmap = renderSign(
                    matrixSet.sign,
                    maxHeight,
                    mPaint
                ),
                equalsSignBitmap = renderSign(
                    equalsSign,
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

            matrixSizeSet[3] = getSignSize(matrixSet.sign, mPaint)
            matrixSizeSet[4] = getSignSize(equalsSign, mPaint)

            return Pair(
                matrixSizeSet.sumByDouble { it.first.toDouble() }.toFloat(),
                (matrixSizeSet.maxBy { it.second } ?: Pair(0.0f, 0.0f)).second)
        }
    }
}
