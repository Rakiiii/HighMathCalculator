package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.dev.smurf.highmathcalculator.Utils.getComplexNumberWidth
import com.example.smurf.mtarixcalc.Matrix


//Extension for canvas to draw matrix
fun Canvas.drawMatrix(matrix: Matrix, x: Float, y: Float, mPaint: Paint): Pair<Float, Float>
{
    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)

    //spaces size between columns
    val horizontalSpaceSize = CanvasRenderSpecification.getHorizontalSpaceSize(mPaint)

    val verticalSpaceSize = CanvasRenderSpecification.getVerticalSpaceSize(mPaint)

    //matrix of render positions
    //val renderMatrix: Array<Array<Float>> = Array(matrix.width, { Array(matrix.height, { 0.0f }) })
    val renderMatrix: Array<Array<Float>> = Array(matrix.height, { Array(matrix.width, { 0.0f }) })

    //matrix rows vertical offset
    val verticalRowsOffset: Array<Pair<Float, Boolean>> = Array(matrix.height, { Pair(0.0f, false) })

    //array that contains max width of elem in every column
    val maxLengthArray = FloatArray(matrix.width)

    //finding longest elem in every column
    for (i in 0 until matrix.width)
    {
        for (j in 0 until matrix.height)
        {
            if (maxLengthArray[i] < mPaint.getComplexNumberWidth(matrix.matrices[j][i]))
            {
                maxLengthArray[i] = mPaint.getComplexNumberWidth(matrix.matrices[j][i])
            }
        }
    }

    //counting offsets for elems of matrix
    for (j in 0 until matrix.width)
    {
        //horizontal offset incase of previous colums
        var horizontalOffset = 0.0f
        for (k in 0 until j)
        {
            horizontalOffset += maxLengthArray[k] + horizontalSpaceSize
        }

        //elems offset in case of max elem length
        for (i in 0 until matrix.height)
        {
            renderMatrix[i][j] =
                horizontalOffset + (maxLengthArray[j] -
                        mPaint.getComplexNumberWidth(matrix.matrices[i][j])) / 2
        }
    }

    //counting vertical offset for every row
    for (i in 0 until matrix.height)
    {
        var rowFlag = false
        for (j in 0 until matrix.width)
        {
            if (!matrix.matrices[i][j].im.isInt() || !matrix.matrices[i][j].re.isInt())
            {
                rowFlag = true
            }
        }

        //if contains fractions then offset should as fraction high that contains 2 numbers high,line and 2 letter spacing
        //and we should have space between lines with size of verticalSpaceSize
        //and offset of preveous raw
        if (rowFlag)
        {
            if (i != matrix.height - 1)
            {
                verticalRowsOffset[i + 1] = Pair(
                    verticalRowsOffset[i].first + 2 * CanvasRenderSpecification.letterVerticalSpacing + 2 * high + verticalSpaceSize + mPaint.strokeWidth,
                    false
                )
            }
            verticalRowsOffset[i] = Pair(verticalRowsOffset[i].first, true)

        }
        else
        {
            if (i != matrix.height - 1)
            {
                //if no fraction in prev row offset shoukd be as size of number plus vertical space plus prev row offset
                verticalRowsOffset[i + 1] = Pair(verticalRowsOffset[i].first + high + verticalSpaceSize, false)
            }
            verticalRowsOffset[i] = Pair(verticalRowsOffset[i].first, false)
        }
    }

    //draw numbers elems
    for (i in matrix.matrices.indices)
    {
        for (j in matrix.matrices[i].indices)
        {
            if (verticalRowsOffset[i].second)
            {
                if (matrix.matrices[i][j].re.isInt() && matrix.matrices[i][j].im.isInt())
                {
                    this.drawComplex(
                        matrix.matrices[i][j],
                        x + renderMatrix[i][j],
                        y + verticalRowsOffset[i].first + high / 2 + CanvasRenderSpecification.letterVerticalSpacing + mPaint.strokeWidth / 2,
                        mPaint
                    )
                }
                else this.drawComplex(
                    matrix.matrices[i][j],
                    x + renderMatrix[i][j],
                    y + verticalRowsOffset[i].first,
                    mPaint
                )
            }
            else this.drawComplex(
                matrix.matrices[i][j],
                x + renderMatrix[i][j],
                y + verticalRowsOffset[i].first,
                mPaint
            )
        }
    }

    return Pair(
        maxLengthArray.sum() + horizontalSpaceSize * (matrix.width - 1),
        if (verticalRowsOffset[matrix.height - 1].second)
            verticalRowsOffset[matrix.height - 1].first + 2 * CanvasRenderSpecification.letterVerticalSpacing + 2 * high + mPaint.strokeWidth
        else
            verticalRowsOffset[matrix.height - 1].first + high
    )

}

//Extension for canvas to draw matrix in brackets
fun Canvas.drawMatrixInBrackets(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)

    //counte bracket width
    val bracketWidth = CanvasRenderSpecification.getBracketWidth(mPaint, matrix.height)

    //draw matrix and get matrix size
    val matrixSize = this.drawMatrix(matrix, x + 2 * bracketWidth / 3, y + high / 4, mPaint)

    //save old style
    val style = mPaint.style

    //set new style
    mPaint.style = Paint.Style.STROKE

    //set rectangle for left bracket draw
    val leftBracketRectangle = RectF(
        x,
        y,
        x + bracketWidth, y + matrixSize.second
    )

    //set rectangle for right bracket draw
    val rightBracketRectangle = RectF(
        x + matrixSize.first + bracketWidth / 2,
        y,
        x + 3 * bracketWidth / 2 + matrixSize.first, y + matrixSize.second
    )

    //draw left bracket
    this.drawArc(leftBracketRectangle, 250.0f, -140.0f, false, mPaint)

    //draw right bracket
    this.drawArc(rightBracketRectangle, 290.0f, 140.0f, false, mPaint)

    //restore old style
    mPaint.style = style
}

//Extension for canvas to draw matrix in Lines
fun Canvas.drawMatrixInLines(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)

    //counte line width
    val lineWidth = CanvasRenderSpecification.getLineWidth(mPaint)

    //draw matrix and get matrix size
    val matrixSize = this.drawMatrix(matrix, x + lineWidth, y + high / 4, mPaint)

    //save old style
    val style = mPaint.style

    //set new style
    mPaint.style = Paint.Style.STROKE

    this.drawLine(x, y, x, y + matrixSize.second, mPaint)

    this.drawLine(
        x + matrixSize.first + lineWidth + 2 * lineWidth / 3,
        y,
        x + matrixSize.first + lineWidth + 2 * lineWidth / 3,
        y + matrixSize.second,
        mPaint
    )

    //restore old style
    mPaint.style = style
}
