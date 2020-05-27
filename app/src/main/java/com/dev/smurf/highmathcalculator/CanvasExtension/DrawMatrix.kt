package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.PaintExtension.*

/*
//Extension for canvas to draw matrix
fun Canvas.drawMatrix(matrix: Matrix, x: Float, y: Float, mPaint: Paint): Pair<Float, Float>
{
    //high of the letter of the setted font
    val high = mPaint.getBaseHeight()//CanvasRenderSpecification.getLetterHigh(mPaint)

    //spaces size between columns
    val horizontalSpaceSize = mPaint.getMatrixHorizontalSpaceSize()//CanvasRenderSpecification.getHorizontalSpaceSize(mPaint)

    val verticalSpaceSize = mPaint.getMatrixVerticalSpaceSize()//CanvasRenderSpecification.getVerticalSpaceSize(mPaint)

    //matrix of render positions
    //val renderMatrix: Array<Array<Float>> = Array(matrix.width, { Array(matrix.height, { 0.0f }) })
    val renderMatrix: Array<Array<Float>> = Array(matrix.height) { Array(matrix.width, { 0.0f }) }

    //matrix rows vertical offset
    val verticalRowsOffset: Array<Pair<Float, Boolean>> = Array(matrix.height) { Pair(0.0f, false) }

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

}*/

fun Canvas.drawMatrix(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    if (matrix.isEmpty()) return
    if (matrix.isNumber()) return drawComplex(matrix.Number(), x, y, mPaint)

    val horizontalSpaceSize = mPaint.getMatrixHorizontalSpaceSize()
    val verticalSpaceSize = mPaint.getMatrixVerticalSpaceSize()

    //matrix size is size of longest elements in each row with spaces and height of highest elements in each column
    val matrixElementsSizeSet = Array(matrix.height) { Array(matrix.width) { Pair(0.0f, 0.0f) } }

    //array of heightens elements in each row
    val maxHeightElements = Array(matrix.height) { 0.0f }

    for (i in matrix.rowIndices())
    {
        var maxHeightInThisRow = 0.0f
        for (j in matrix.columnIndices())
        {
            matrixElementsSizeSet[i][j] = mPaint.getComplexNumberSize(matrix[i, j])
            maxHeightInThisRow =
                if (matrixElementsSizeSet[i][j].second > maxHeightInThisRow)
                    matrixElementsSizeSet[i][j].second
                else maxHeightInThisRow
        }

        maxHeightElements[i] = maxHeightInThisRow
    }


    //get max width of elements in each column
    //not the best way, but use origin matrix size instead of iteration over @matrixElementsSizeSet makes semantics easier
    val maxWidthElements = Array(matrix.width) { 0.0f }

    for (j in matrix.columnIndices())
    {
        var maxLength = 0.0f
        for (i in matrix.rowIndices())
        {
            maxLength =
                if (matrixElementsSizeSet[i][j].first > maxLength) matrixElementsSizeSet[i][j].first else maxLength
        }

        maxWidthElements[j] = maxLength
    }

    /*
    cus of complex number contracts, render of matrix of complex numbers is render of matrix of rectangles, where we now sizes
    so  renders come to calculating offsets of left angel of rectangle from origin (x,y)
    matrix of elements of set, where first is horizontal offset and second is vertical offset
    */
    val offsetMatrix = Array(matrix.height) { Array(matrix.width) { Pair(0.0f, 0.0f) } }

    /*
    vertical offset is (differences between max height of element in row and self height) divided by 2 plus sum
    of max height of elements in prev row plus spaces
    horizontal offset is (differences between max width of element in column and self width) divided by 2 plus
    sum of max width of element in previous columns plus vertical spaces
    */

    var prevVerticalOffset = 0.0f

    for (i in matrixElementsSizeSet.indices)
    {
        var prevHorizontalOffset = 0.0f
        for (j in matrixElementsSizeSet[i].indices)
        {
            val horizontalOffset =
                ((maxWidthElements[j] - matrixElementsSizeSet[i][j].first) / 2) +
                        prevHorizontalOffset +
                        (j * horizontalSpaceSize)
            //increase horizontal offset for prev element in row
            prevHorizontalOffset += maxWidthElements[j]

            val verticalOffset = ((maxHeightElements[i] - matrixElementsSizeSet[i][j].second) / 2) +
                    prevVerticalOffset +
                    (i * verticalSpaceSize)


            offsetMatrix[i][j] = Pair(horizontalOffset, verticalOffset)
        }
        //increase vertical offset of prev elements in column
        prevVerticalOffset += maxHeightElements[i]
    }

    for (i in matrix.rowIndices())
    {
        for (j in matrix.columnIndices())
        {
            drawComplex(
                matrix[i, j],
                x + offsetMatrix[i][j].first,
                y + offsetMatrix[i][j].second,
                mPaint
            )
        }
    }
}

//Extension for canvas to draw matrix in brackets
fun Canvas.drawMatrixInBrackets(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    if (matrix.isEmpty()) return
    if (matrix.isNumber()) return drawComplex(matrix.matrices[0][0], x, y, mPaint)

    val matrixSize = mPaint.getMatrixSize(matrix)

    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)

    //count bracket width
    val bracketWidth = CanvasRenderSpecification.getBracketWidth(mPaint, matrix.height)

    //draw matrix
    this.drawMatrix(matrix, x + 2 * bracketWidth / 3, y + high / 4, mPaint)

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
    if (matrix.isEmpty()) return
    if (matrix.isNumber()) return drawComplex(matrix.matrices[0][0], x, y, mPaint)

    val matrixSize = mPaint.getMatrixSize(matrix)

    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)

    //count line width
    val lineWidth = mPaint.strokeWidth

    //draw matrix
    this.drawMatrix(matrix, x+lineWidth*2 , y, mPaint)

    //save old style
    val style = mPaint.style

    this.drawLine(x, y, x, y + matrixSize.second, mPaint)

    this.drawLine(
        x + matrixSize.first +  4 * lineWidth,
        y+mPaint.getMatrixVerticalOffset(),
        x + matrixSize.first  +  4 * lineWidth,
        y + matrixSize.second+mPaint.getMatrixVerticalOffset(),
        mPaint
    )
}


fun Canvas.drawMatrixInBracketsAsDots(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    if (matrix.isEmpty()) return
    drawProporsionalDotsInBrackets(3, x, y, mPaint)
}

fun Canvas.drawMatrixInLinesAsDots(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    if (matrix.isEmpty()) return

    drawProporsionalDotsInLines(3, x, y, mPaint.getProportionalDotsRadius(), mPaint)
}