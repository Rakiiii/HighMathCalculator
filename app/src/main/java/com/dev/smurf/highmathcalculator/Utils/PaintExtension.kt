package com.dev.smurf.highmathcalculator.Utils

import android.graphics.Paint
import android.graphics.Rect
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.example.smurf.mtarixcalc.Matrix

//todo:: add matrix seize counting extension

//returns length of @fraction in paint param
fun Paint.getFractionWidth(fraction: Fraction): Float
{
    //overall length of fraction
    var overallLength = 0.0f

    //if fraction is not a fraction
    if (fraction.isInt())
    {
        //counting length of upper number
        val arr = FloatArray(fraction.upper.toString().length)
        this.getTextWidths(fraction.upper.toString(), arr)
        return arr.sum()
    }


    //if fraction contains minus symbol
    if (fraction.isBeloweZero())
    {
        //counting length of minus symbol
        val arr = FloatArray(1)
        this.getTextWidths("-", arr)

        //increase overall length with length of minus symbol
        overallLength += arr.sum()
    }

    //array with length of characters in upper number of fraction
    val upperValueWidth = FloatArray(fraction.upper.toString().length)
    //array with length of characters in lower number of fraction
    val lowerValueWidth = FloatArray(fraction.lower.toString().length)

    //counting max length of number in fraction
    if (this.getTextWidths(
            fraction.upper.toString(),
            upperValueWidth
        ) > this.getTextWidths(fraction.lower.toString(), lowerValueWidth)
    )
    {
        //if upper number longer then increase overall length with its length
        overallLength += upperValueWidth.sum()

    }
    else
    {
        //if lower number longer then increase overall length with its length
        overallLength += lowerValueWidth.sum()

    }

    return overallLength
}

fun Paint.getComplexNumberWidth(complexNumber: ComplexNumber): Float
{
    var overallLength = 0.0f

    when
    {
        //if complex number is purely real
        complexNumber.isReal() ->
        {
            //it will draw only real part
            return this.getFractionWidth(complexNumber.re)
        }

        //if complex number is purely imaginary
        complexNumber.isImagination() ->
        {
            //its length composes length of i and length of imaginary part
            //get i length
            val arr = FloatArray(1)
            this.getTextWidths("i", arr)
            overallLength += arr.sum()

            //get imaginary part length
            overallLength += this.getFractionWidth(complexNumber.im)

            return overallLength

        }
    }

    //if complex number is full
    val arr = FloatArray(4)

    //if imaginary part is below zero
    if (complexNumber.im.isBeloweZero())
    {
        //count length of symbol with minus
        this.getTextWidths("(-i)", arr)
        overallLength += arr.sum()
    }
    else
    {
        //count length of sybol with plus in other case
        this.getTextWidths("(+i)", arr)
        overallLength += arr.sum()
    }

    //count length of real part
    overallLength += this.getFractionWidth(complexNumber.re)

    //count length of imaginary part
    overallLength += this.getFractionWidth(complexNumber.im)

    return overallLength

}

fun Paint.getComplexNumberHigh(complexNumber: ComplexNumber): Float
{
    if (complexNumber.re.isInt() && complexNumber.im.isInt())
    {
        return CanvasRenderSpecification.getLetterHigh(this)
    }
    else
    {
        return 2 * CanvasRenderSpecification.getLetterHigh(this) +
                2 * CanvasRenderSpecification.letterVerticalSpacing +
                CanvasRenderSpecification.getLineWidth(
                    this
                )
    }
}


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
    //counte bracket width
    val bracketWidth = CanvasRenderSpecification.getBracketWidth(this, matrix.height)

    val matrixSise = this.getMatrixSize(matrix)

    return Pair(matrixSise.first + 2 * bracketWidth, matrixSise.second)
}


fun Paint.getMatrixInLinesSize(matrix: Matrix): Pair<Float, Float>
{
    //counte bracket width
    val lineWidth = CanvasRenderSpecification.getLineWidth(this)

    val matrixSise = this.getMatrixSize(matrix)

    return Pair(matrixSise.first + 2 * lineWidth, matrixSise.second)
}

fun Paint.getPolynomialWidth(polynomial: PolynomialBase): Float
{
    val renderSet = polynomial.renderFormat()

    var overallWidth = 0.0f

    for (i in renderSet)
    {
        overallWidth += this.getComplexNumberWidth(i.second)

        val text = i.first + "+"

        val arr = FloatArray(text.length)
        this.getTextWidths(text, arr)
        overallWidth += arr.sum()
    }



    return overallWidth
}

fun Paint.getPolynomialHigh(polynomial: PolynomialBase): Float
{
    for (i in polynomial.renderFormat())
    {
        if (i.second.containsFractions()) return  CanvasRenderSpecification.getLetterHigh(this) + 2 * CanvasRenderSpecification.getVerticalSpaceSize(
            this
        ) + CanvasRenderSpecification.getLineWidth(this)
    }

    val textRect = Rect()
    this.getTextBounds(polynomial.renderFormat()[0].first,0,polynomial.renderFormat()[0].first.length,textRect)

    return 2*textRect.height().toFloat() + CanvasRenderSpecification.PolynomialBottomMargin + CanvasRenderSpecification.PolynomialTopMargin
    //return CanvasRenderSpecification.getLetterHigh(this)
}