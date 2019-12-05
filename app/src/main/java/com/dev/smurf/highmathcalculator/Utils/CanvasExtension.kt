package com.dev.smurf.highmathcalculator.Utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.example.smurf.mtarixcalc.Matrix
import kotlin.math.absoluteValue

//Space vertical thickness between separating line and number in fraction
val letterVerticalSpacing = 5.0f

//Extension for canvas to draw custom fraction returns offset of middle of fraction
fun Canvas.drawFraction(fraction: Fraction, x: Float, y: Float, mPaint: Paint) : Float
{

    //thicknes of separating line in fraction
    val lineThickness = mPaint.strokeWidth

    //horizontal offset of fraction
    var horizontalOffset = 0.0f

    //cof for rendering lower in fraction
    val lowerOffsetCof = 0.7f

    //font metrics
    val fm = mPaint.fontMetrics

    //high of the letter of the setted font
    val high = fm.descent - fm.ascent

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
            val minusVerticalPozition = y + high + letterVerticalSpacing
            this.drawText("-", x + horizontalOffset, minusVerticalPozition, mPaint)

            //getting width of minus in setted offset
            val arr = FloatArray(2)
            mPaint.getTextWidths("-", arr)

            //increment horizontal offset on minus size
            horizontalOffset += arr[0]
        }
    }

    //vertical offset of y for separating line position
    val lineVerticalPosition = y + high + letterVerticalSpacing

    //array with length of characters in upper number of fraction
    val upperValueWidth = FloatArray(fraction.upper.toString().length)
    //array with length of characters in lower number of fraction
    val lowerValueWidth = FloatArray(fraction.lower.toString().length)

    //width of separating line
    val lineWidth : Float
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
        lineVerticalPosition + high*lowerOffsetCof ,
        mPaint
    )

    return lineVerticalPosition
}


fun Canvas.drawComplex(complexNumber: ComplexNumber, x : Float, y : Float, mPaint: Paint) //: Float
{

    //font metrics
    val fm = mPaint.fontMetrics

    //high of the letter of the setted font
    val high = fm.descent - fm.ascent

    when
    {
        complexNumber.isReal()->
        {
            //if real number render real part
            this.drawFraction(complexNumber.re , x , y , mPaint )

            return
        }

        complexNumber.isImagination()->
        {
            //if imegination number render imeginary part and count i pos
            val iVerticalPos = this.drawFraction(complexNumber.im,x,y,mPaint) + high/2

            val iHorizontalPos = mPaint.getFractionWidth(complexNumber.im)

            this.drawText("i" , x + iHorizontalPos , y + iVerticalPos , mPaint)

            return //iVerticalPos - high/2
        }
    }

    val arr = FloatArray(1)
    mPaint.getTextWidths("(" , arr)
    val lengthOfLeftBracket = arr.sum()

    var horizontalPosition = x + lengthOfLeftBracket

    val middlePositionOfRealPart = this.drawFraction(complexNumber.re , horizontalPosition , y , mPaint)

    horizontalPosition += mPaint.getFractionWidth(complexNumber.re)
    val verticalPositionOfSign = y + middlePositionOfRealPart - high/2 - letterVerticalSpacing

    this.drawText( "(" , x , verticalPositionOfSign , mPaint )

    if( complexNumber.im.isBeloweZero())
    {
        this.drawText("-" , horizontalPosition , verticalPositionOfSign , mPaint )

        mPaint.getTextWidths("+",arr)

        horizontalPosition += arr.sum()
    }
    else
    {
        this.drawText("+" , horizontalPosition , verticalPositionOfSign , mPaint )

        mPaint.getTextWidths("+",arr)

        horizontalPosition += arr.sum()
    }

    val absoluteFraction = Fraction( _upper = complexNumber.im.upper.absoluteValue , _lower = complexNumber.im.lower.absoluteValue)

    val middlePositionOfImagionaryPart = this.drawFraction( absoluteFraction , horizontalPosition , y , mPaint )

    horizontalPosition += mPaint.getFractionWidth(absoluteFraction)

    val imVerticalPosition = y + middlePositionOfImagionaryPart - high/2 - letterVerticalSpacing

    this.drawText("i)" , horizontalPosition , imVerticalPosition , mPaint )

}

fun Canvas.drawMatrix(matrix: Matrix, x : Float, y : Float, mPaint: Paint, isDeterminant : Boolean)
{
    var stX: Float = x + mPaint.textSize / 2

    var stY: Float = y + mPaint.textSize * 3 / 4

    var maxLength  : Int

    for( i in 0 until matrix.width )
    {
        maxLength = 0
        for (j in 0 until matrix.height)
        {
            this.drawComplex(matrix.matrices[j][i], stX, stY + (j * 2 * mPaint.textSize), mPaint)
            if(matrix.matrices[j][i].length() > maxLength) maxLength = matrix.matrices[i][j].length()
        }

        stX = stX + (maxLength + 1)*mPaint.textSize
    }


    val style = mPaint.style

    mPaint.style = Paint.Style.STROKE

    if(isDeterminant)
    {
        this.drawLine(x,y , x , y + ( matrix.height * ( matrix.maxFractionsInColumn() - 1 ) ) * mPaint.textSize , mPaint )

        this.drawLine(stX , y , stX , y + ( matrix.height * ( matrix.maxFractionsInColumn() - 1 ) ) * mPaint.textSize , mPaint )
    }
    else
    {
        val rectangle: RectF = RectF(
            x,
            y,
            x + mPaint.textSize / 2,
            y + ( matrix.height * ( matrix.maxFractionsInColumn() - 1 ) ) * mPaint.textSize
        )

        this.drawArc(rectangle, 100.0f, 170.0f, false, mPaint)

        rectangle.set(stX ,
            y ,
            stX + mPaint.textSize / 2 ,
            y + ( matrix.height *( matrix.maxFractionsInColumn() - 1 ) ) * mPaint.textSize )

        this.drawArc(rectangle, 80.0f, -170.0f, false, mPaint)
    }

    mPaint.style = style

}

/*

*val ts = mPaint.textSize/2 + 3
    val mv = mPaint.textSize/2

    if(fraction.isInt())
    {
        this.drawText(fraction.upper.toString(), x, y, mPaint)
    }
    else
    {
        if(fraction.isBeloweZero())
        {
            this.drawText("-" , x , y, mPaint)

            var mX = x + mPaint.textSize - 10

            this.drawText(fraction.upper.toString().substringAfter('-'), mX, y - ts, mPaint)

            this.drawText(fraction.lower.toString(), mX, y + ts , mPaint)

            val style = mPaint.style
            mPaint.style = Paint.Style.STROKE

            this.drawLine(
                mX - 5,
                y-mv,
                mX + fraction.maxLenght() * (mPaint.textSize * ( 2/ ( fraction.maxLenght() + 2 ) ) - 5 ) - 5,
                y-mv,
                mPaint
            )

            mPaint.style = style
        }
        else
        {
            this.drawText(fraction.upper.toString(), x , y-ts, mPaint)

            this.drawText(fraction.lower.toString(), x, y + ts, mPaint)

            val style = mPaint.style
            mPaint.style = Paint.Style.STROKE

            this.drawLine(
                x - 5,
                y - mv,
                x + fraction.maxLenght() * ( ( mPaint.textSize * ( 2/ ( fraction.maxLenght() + 1 )  ) ) - 5 ) - 5,
                y - mv,
                mPaint
            )

            mPaint.style = style
        }
    }
 *  */

/*
  if(complexNumber.isReal())this.drawFraction( complexNumber.re , x , y , mPaint )
    else
    {
        if(complexNumber.isImagination())
        {
            this.drawFraction( complexNumber.im , x , y , mPaint)
            this.drawText("i" , x + complexNumber.im.maxLenght() * mPaint.textSize , y , mPaint)
        }
        else
        {
            val ts = mPaint.textSize

            this.drawText("(", x, y, mPaint)

            this.drawFraction(complexNumber.re, x + ts, y, mPaint)

            var imX = x + complexNumber.re.maxLenght() * ts

            if (!complexNumber.im.isBeloweZero())
            {
                this.drawText("+", imX, y, mPaint)
                imX += ts
            }
            this.drawFraction(complexNumber.im, imX, y, mPaint)
            this.drawText("i)", imX + complexNumber.im.maxLenght() * ts, y, mPaint)
        }
    }
    */
