package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.dev.smurf.highmathcalculator.Matrix.Matrix
import com.dev.smurf.highmathcalculator.Matrix.MatrixLine
import com.dev.smurf.highmathcalculator.PaintExtension.*


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

fun Canvas.drawBracket(
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    mPaint: Paint,
    isLeft: Boolean
)
{

    val bracketRect = RectF(x, y, x + width, y + height)

    val rotatoryParam = if (isLeft) 1.0f else -1.0f

    drawArc(bracketRect, 90.0f, rotatoryParam * 180.0f, false, mPaint)

}

//Extension for canvas to draw matrix in brackets
fun Canvas.drawMatrixInBrackets(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    if (matrix.isEmpty()) return
    if (matrix.isNumber()) return drawComplex(matrix.matrices[0][0], x, y, mPaint)

    val matrixSize = mPaint.getMatrixSize(matrix)

    val bracketWidth = mPaint.getMatrixHorizontalOffset()

    //draw matrix
    this.drawMatrix(matrix, x + bracketWidth, y + mPaint.getMatrixUpperOffset(), mPaint)

    //save old style
    val style = mPaint.style

    //set new style
    mPaint.style = Paint.Style.STROKE

    val overallMatrixHeight =
        matrixSize.second + mPaint.getMatrixUpperOffset() + mPaint.getMatrixLowerOffset()

    drawBracket(x, y, bracketWidth, overallMatrixHeight, mPaint, true)

    drawBracket(
        x + bracketWidth + matrixSize.first,
        y,
        bracketWidth,
        overallMatrixHeight,
        mPaint,
        false
    )

    //restore old style
    mPaint.style = style
}

//Extension for canvas to draw matrix in Lines
fun Canvas.drawMatrixInLines(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    if (matrix.isEmpty()) return
    if (matrix.isNumber()) return drawComplex(matrix.matrices[0][0], x, y, mPaint)

    val matrixSize = mPaint.getMatrixSize(matrix)

    //count line width
    val lineWidth = mPaint.strokeWidth

    //draw matrix
    this.drawMatrix(matrix, x + lineWidth * 2, y + mPaint.getMatrixUpperOffset(), mPaint)

    //save old style
    val style = mPaint.style

    this.drawLine(
        x,
        y,
        x,
        y + matrixSize.second + mPaint.getMatrixUpperOffset() + mPaint.getMatrixLowerOffset(),
        mPaint
    )

    this.drawLine(
        x + matrixSize.first + 4 * lineWidth,
        y,
        x + matrixSize.first + 4 * lineWidth,
        y + matrixSize.second + mPaint.getMatrixUpperOffset() + mPaint.getMatrixLowerOffset(),
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

fun Canvas.drawMatrixAsSetOfVectors(matrix: Matrix, x: Float, y: Float, mPaint: Paint)
{
    if (matrix.isEmpty()) return
    val vectors = matrix.asVectors()
    var horizontalOffset = x

    val vectorsSizes = Array(vectors.size) { s -> mPaint.getMatrixInBracketsSize(vectors[s]) }
    val maxHeight = (vectorsSizes.maxBy { s -> s.second } ?: Pair(0.0f, 0.0f)).second

    var verticalOffset = Array(vectors.size) { s -> (maxHeight - vectorsSizes[s].second) / 2 }

    for (i in vectors.indices)
    {
        drawMatrixInBrackets(
            matrix = vectors[i],
            x = horizontalOffset,
            y = y + verticalOffset[i],
            mPaint = mPaint
        )

        horizontalOffset += vectorsSizes[i].first
    }
}