package com.dev.smurf.highmathcalculator.Utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialRoots
import com.dev.smurf.highmathcalculator.Utils.CanvasRenderSpecification.Companion.letterVerticalSpacing
import com.example.smurf.mtarixcalc.Matrix
import kotlin.math.absoluteValue


//Extension for canvas to draw custom fraction returns offset of middle of fraction
fun Canvas.drawFraction(fraction: Fraction, x: Float, y: Float, mPaint: Paint): Float
{

    //thicknes of separating line in fraction
    val lineThickness = mPaint.strokeWidth

    //horizontal offset of fraction
    var horizontalOffset = 0.0f

    //cof for rendering lower in fraction
    val lowerOffsetCof = 0.7f

    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)

    when
    {
        //Drawing number in case of non fractoin value
        fraction.isInt() ->
        {

            //counting vertical offsetn to draw number in the middle of possible fraction
            val verticalOffset = (2 * high + lineThickness + 2 * letterVerticalSpacing) / 4

            //drawing number
            this.drawText(fraction.upper.toString(), x + horizontalOffset, y + verticalOffset, mPaint)
            return verticalOffset * 2
        }
        //Drawing fraction with minus
        fraction.isBeloweZero() ->
        {
            //Drawing minus
            val minusVerticalPozition = y + high + letterVerticalSpacing*3
            this.drawText("-", x + horizontalOffset, minusVerticalPozition, mPaint)

            //getting width of minus in setted offset
            val arr = FloatArray(2)
            mPaint.getTextWidths("-", arr)

            //increment horizontal offset on minus size
            horizontalOffset += arr[0]+CanvasRenderSpecification.defspaceSize
        }
    }

    //vertical offset of y for separating line position
    val lineVerticalPosition = y + high + letterVerticalSpacing

    //array with length of characters in upper number of fraction
    val upperValueWidth = FloatArray(fraction.upper.toString().length)
    //array with length of characters in lower number of fraction
    val lowerValueWidth = FloatArray(fraction.lower.toString().length)

    //width of separating line
    val lineWidth: Float
    //offset of upper number in fraction
    var upperOffset = 0.0f
    //offset of lower number in fraction
    var lowerOffset = 0.0f

    //counting max length of number in fraction
    if (mPaint.getTextWidths(
            fraction.upper.toString(),
            upperValueWidth
        ) > mPaint.getTextWidths(fraction.lower.toString(), lowerValueWidth)
    )
    {
        //if upper number longer then linew width is width of upper number
        lineWidth = upperValueWidth.sum()

        //in this case lower number must start from some offset to in the middle of line
        lowerOffset = (upperValueWidth.sum() - lowerValueWidth.sum()) / 2
    }
    else
    {
        //if lower number longer then linew width is width of upper number
        lineWidth = lowerValueWidth.sum()

        //in this case upper number must start from some offset to in the middle of line
        upperOffset = (lowerValueWidth.sum() - upperValueWidth.sum()) / 2
    }


    //draw separating line
    this.drawLine(
        x + horizontalOffset,
        lineVerticalPosition,
        x + horizontalOffset + lineWidth,
        lineVerticalPosition,
        mPaint
    )

    //draw upper number
    this.drawText(
        fraction.upper.absoluteValue.toString(),
        x + horizontalOffset + upperOffset,
        lineVerticalPosition - letterVerticalSpacing,
        mPaint
    )

    //draw lower number
    this.drawText(
        fraction.lower.toString(),
        x + horizontalOffset + lowerOffset,
        lineVerticalPosition + high * lowerOffsetCof,
        mPaint
    )

    return lineVerticalPosition
}


//Extension for canvas to draw complex number
fun Canvas.drawComplex(complexNumber: ComplexNumber, x: Float, y: Float, mPaint: Paint) //: Float
{

    //high of the letter of the setted font
    val high = CanvasRenderSpecification.getLetterHigh(mPaint)
    when
    {
        //if number purely real then draw only real part
        complexNumber.isReal() ->
        {
            //if real number render real part
            this.drawFraction(complexNumber.re, x, y, mPaint)

            return
        }

        //if number purly imaginary draw only imageronary part
        complexNumber.isImagination() ->
        {
            //if imegination number render imeginary part and count i pos
            val iVerticalPos = this.drawFraction(complexNumber.im, x, y, mPaint) + high / 2

            val iHorizontalPos = mPaint.getFractionWidth(complexNumber.im)

            this.drawText("i", x + iHorizontalPos, y + iVerticalPos, mPaint)

            return //iVerticalPos - high/2
        }
    }

    //counting length of left bracket
    val arr = FloatArray(1)
    mPaint.getTextWidths("(", arr)
    val lengthOfLeftBracket = arr.sum()

    //move horizontal position for drawing to the left of bracket
    var horizontalPosition = x + lengthOfLeftBracket

    //draw real part of number and get offset of middle of drawed number
    val middlePositionOfRealPart = this.drawFraction(complexNumber.re, horizontalPosition, y, mPaint)

    //move horizontal position to the left of real part
    horizontalPosition += mPaint.getFractionWidth(complexNumber.re)

    //calculate vertical position of left bracket and sign
    val verticalPositionOfSign = y + middlePositionOfRealPart - high / 2 - letterVerticalSpacing

    //draw left bracket
    this.drawText("(", x, verticalPositionOfSign, mPaint)

    //if imaginary part is below zero
    if (complexNumber.im.isBeloweZero())
    {
        //draw minus
        this.drawText("-", horizontalPosition, verticalPositionOfSign, mPaint)

        //getting width of minus symbol
        mPaint.getTextWidths("-", arr)

        //move horizontal position to the left of minus
        horizontalPosition += arr.sum()
    }
    else
    {
        //draw plus in other case
        this.drawText("+", horizontalPosition, verticalPositionOfSign, mPaint)

        //getting width of plus symbol
        mPaint.getTextWidths("+", arr)

        //moving horizontal position to the left of plus
        horizontalPosition += arr.sum()
    }

    //init absolute version of imaginary part of complex number, because minus is already drawn
    val absoluteFraction =
        Fraction(_upper = complexNumber.im.upper.absoluteValue, _lower = complexNumber.im.lower.absoluteValue)

    //draw imaginary part of number and get offset of middle of imaginary part
    val middlePositionOfImaginaryPart = this.drawFraction(absoluteFraction, horizontalPosition, y, mPaint)

    //move horizontal position of drawing to the left of imaginary part
    horizontalPosition += mPaint.getFractionWidth(absoluteFraction)

    //calculate vertical position of right bracket and imaginary unit
    val imVerticalPosition = y + middlePositionOfImaginaryPart - high / 2 - letterVerticalSpacing

    //draw right bracket and imaginary un it
    this.drawText("i)", horizontalPosition, imVerticalPosition, mPaint)

}

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
                    verticalRowsOffset[i].first + 2 * letterVerticalSpacing + 2 * high + verticalSpaceSize + mPaint.strokeWidth,
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
                        y + verticalRowsOffset[i].first + high / 2 + letterVerticalSpacing + mPaint.strokeWidth / 2,
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
            verticalRowsOffset[matrix.height - 1].first + 2 * letterVerticalSpacing + 2 * high + mPaint.strokeWidth
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

//draw polinom on canvas returns last point of polynomial render and vertical offset for render signs
fun Canvas.drawPolynomial(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint): Pair<Float, Float>
{
    val polynomialForRender = polynomial.renderFormat()

    //flag of containtment of fraction in cofs of polynomial
    var fractionFlag = false

    //check for fractions in cofs
    for (i in polynomialForRender)
    {
        if (!i.second.im.isInt() || !i.second.re.isInt())
        {
            fractionFlag = true
            break
        }
    }


    //vertical offset for symbols,signs and non fraction numbers
    val verticalOffset: Float = y +
            if (fractionFlag) (2 * CanvasRenderSpecification.getLetterHigh(mPaint) +
                    CanvasRenderSpecification.getVerticalSpaceSize(mPaint) +
                    CanvasRenderSpecification.getLineWidth(mPaint)) / 2
            else 0.0f//CanvasRenderSpecification.getLetterHigh(mPaint) / 2

    //horizontal offset for render those element
    var horizontalOffset = x

    //count amount of rendered cofs
    var renderedCounter = 0

    //count amount of itterations
    var itterationCounter = 0

    //start render
    for (i in polynomialForRender)
    {
        //count itteration
        itterationCounter++

        //if cof before is 0 we dont need to draw it
        if (!i.second.equals(0))
        {
            //count rendered cof
            renderedCounter++

            //if cof contains fraction we dont need any vertical offset
            if (i.second.containsFractions())
            {
                this.drawComplex(i.second, horizontalOffset, y + CanvasRenderSpecification.getLetterHigh(mPaint)/2 + letterVerticalSpacing, mPaint)
            }
            else
            {
                //if cofs dont contains fraction we need counted vertical offset
                this.drawComplex(i.second, horizontalOffset, verticalOffset, mPaint)
            }

            //update horizontal offset to the left of rendered cof
            //we dont need to render plus after last cof
            horizontalOffset += mPaint.getComplexNumberWidth(i.second)

            //init text for render after cof

            val text = i.first + if (i == polynomialForRender.last() || (i.second.isBelowZero())) "" else "+"

            //draw text it self
            this.drawText(text, horizontalOffset, verticalOffset + CanvasRenderSpecification.getLetterHigh(mPaint)/2, mPaint)

            //calculate horizontal offset
            //we need an array if size word to get length of all symbols in the word
            val arr = FloatArray(text.length)
            //get length of symbols it self
            mPaint.getTextWidths(text, arr)

            //sum symbols length,get length of symbols and update horizontal offset to the left of symbols
            horizontalOffset += arr.sum()
        }
    }

    if (itterationCounter == polynomialForRender.size && renderedCounter == 0)
    {
        //init text for render after cof
        val text = "0"

        //draw text it self
        this.drawText(text, horizontalOffset, verticalOffset + 4.0f, mPaint)

        //calculate horizontal offset
        //we need an array if size word to get length of all symbols in the word
        val arr = FloatArray(text.length)
        //get length of symbols it self
        mPaint.getTextWidths(text, arr)

        //sum symbols length,get length of symbols and update horizontal offset to the left of symbols
        horizontalOffset += arr.sum()
    }

    //returns offset pair
    return Pair(horizontalOffset, verticalOffset)
}

//draw equation that setted by polynomial @polynomial on canvas
fun Canvas.drawEquation(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint)
{
    //draw polynomial
    val offsets = this.drawPolynomial(polynomial, x, y, mPaint)

    //draw equals zero after polynomial to get equation
    this.drawText("=0", offsets.first, offsets.second, mPaint)
}

fun Canvas.drawPolynomialRoots(roots: PolynomialRoots, x: Float, y: Float, mPaint: Paint)
{
    var verticalOffset = y
    for (i in roots)
    {
        if (i.second.containsFractions())
        {
            verticalOffset =
                y + CanvasRenderSpecification.getLetterHigh(mPaint) + CanvasRenderSpecification.getVerticalSpaceSize(
                    mPaint
                ) + CanvasRenderSpecification.getLineWidth(mPaint) / 2

            break
        }
    }

    var horizontalOffset = x

    for (i in roots)
    {

        val symbol = i.first + roots.getSepareteSymbol()
        val arr = FloatArray(symbol.length)
        mPaint.getTextWidths(symbol, arr)

        this.drawText(symbol, horizontalOffset, verticalOffset, mPaint)

        horizontalOffset += arr.sum()

        if (i.second.containsFractions())
        {
            this.drawComplex(i.second, horizontalOffset, y, mPaint)
        }
        else
        {
            this.drawComplex(i.second, horizontalOffset, verticalOffset, mPaint)
        }

        horizontalOffset += mPaint.getComplexNumberWidth(i.second)

        horizontalOffset += CanvasRenderSpecification.getHorizontalSpaceSize(mPaint)

    }

}




