package com.dev.smurf.highmathcalculator.PaintExtension

import android.graphics.Paint
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Matrix.Matrix

fun Paint.getMatrixSize(matrix: Matrix): Pair<Float, Float>
{
    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(this)

    //spaces size between columns
    val horizontalSpaceSize = CanvasRenderSpecification.getHorizontalSpaceSize(this)

    val verticalSpaceSize = CanvasRenderSpecification.getVerticalSpaceSize(this)

    //array that contains max width of elem in every column
    val maxLengthArray = FloatArray(matrix.width)

    //finding longest elem in every column
    for (i in 0 until matrix.width)
    {
        for (j in 0 until matrix.height)
        {
            if (maxLengthArray[i] < this.getComplexNumberWidth(matrix.matrices[j][i]))
            {
                maxLengthArray[i] = this.getComplexNumberWidth(matrix.matrices[j][i])
            }
        }
    }

    //calculate max vertical length of matrix
    var overallHigh = 0.0f

    for (i in matrix.matrices.indices)
    {
        var rowFlag = false
        for (j in matrix.matrices[i].indices)
        {
            if (!matrix.matrices[i][j].re.isInt() || !matrix.matrices[i][j].im.isInt())
            {
                rowFlag = true
            }
        }

        if (rowFlag)
        {
            overallHigh += 2 * high + 2 * CanvasRenderSpecification.letterVerticalSpacing + this.strokeWidth
        }
        else
        {
            overallHigh += high
        }
    }

    return Pair(
        maxLengthArray.sum() + horizontalSpaceSize * (matrix.width - 1),
        overallHigh + verticalSpaceSize * (matrix.height - 1)
    )
}

fun Paint.getMatrixInBracketsSize(matrix: Matrix): Pair<Float, Float>
{
    if(matrix.isEmpty() && !matrix.isNumber())return Pair(0.0f,0.0f)
    if (matrix.isNumber())return getComplexNumberSize(matrix.matrices[0][0])
    //counte bracket width
    val bracketWidth = CanvasRenderSpecification.getBracketWidth(this, matrix.height)

    val matrixSise = this.getMatrixSize(matrix)

    return Pair(matrixSise.first + 2 * bracketWidth, matrixSise.second)
}


fun Paint.getMatrixInLinesSize(matrix: Matrix): Pair<Float, Float>
{
    if(matrix.isEmpty() && !matrix.isNumber())return Pair(0.0f,0.0f)
    if (matrix.isNumber())return getComplexNumberSize(matrix.matrices[0][0])
    //counte bracket width
    val lineWidth = CanvasRenderSpecification.getLineWidth(this)

    val matrixSise = this.getMatrixSize(matrix)

    return Pair(matrixSise.first + 2 * lineWidth, matrixSise.second)
}

fun Paint.getMatrixInBracketsAsDotsSize(matrix: Matrix): Pair<Float, Float>
{
    return if(matrix.isEmpty()) Pair(0.0f,0.0f)
    else getProportionalDotsInBracketSize(3)
}

fun Paint.getMatrixInLinesAsDotsSize(matrix: Matrix): Pair<Float, Float>
{
    return if(matrix.isEmpty()) Pair(0.0f,0.0f)
    else getProportionalDotsInLineSize(3,getProportionalDotsRadius())
}

