package com.dev.smurf.highmathcalculator.CanvasExtension

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.dev.smurf.highmathcalculator.PaintExtension.*
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialRoots


//draw polinom on canvas returns last point of polynomial render and vertical offset for render signs
fun Canvas.drawPolynomials(
    polynomial: PolynomialBase,
    x: Float,
    y: Float,
    mPaint: Paint
): Pair<Float, Float>
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


    //vertical offset for symbols,signs
    val textVerticalOffset: Float = y +
            if (fractionFlag) CanvasRenderSpecification.getLetterHigh(mPaint) +
                    (CanvasRenderSpecification.getLetterHigh(mPaint) / 2) -
                    CanvasRenderSpecification.letterVerticalSpacing -
                    CanvasRenderSpecification.getLineWidth(mPaint) / 2
            else CanvasRenderSpecification.getLetterHigh(mPaint)

    //vertical offset for non fraction numbers
    val verticalOffset: Float = y +
            if (fractionFlag) CanvasRenderSpecification.getLetterHigh(mPaint) -
                    CanvasRenderSpecification.letterVerticalSpacing -
                    CanvasRenderSpecification.getLineWidth(mPaint) / 2
            else CanvasRenderSpecification.getLetterHigh(mPaint) / 2

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
                this.drawComplex(i.second, horizontalOffset, y, mPaint)
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

            val text =
                i.first + if (i == polynomialForRender.last() || (i.second.isBelowZero())) "" else "+"

            //draw text it self
            this.drawText(text, horizontalOffset, textVerticalOffset, mPaint)

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

fun Canvas.drawPolynomial(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint) =
    drawPolynomialRange(polynomial, x, y, mPaint, 0 until polynomial.renderFormat().size)


/* draws an range @range of cofs from polynomial @polynomial in position [@x;@y] */
fun Canvas.drawPolynomialRange(
    polynomial: PolynomialBase,
    x: Float,
    y: Float,
    mPaint: Paint,
    range: IntRange
)
{
    //if polynomial is empty we mustn't draw any thing
    if (polynomial == PolynomialBase.EmptyPolynomial || polynomial.degree() == 0) return

    //if range contains wrong position we must not draw any thing
    if (range.first < 0) return

    // if range is too big, we basically must render full polynomial
    if (range.last > polynomial.renderFormat().size) return drawPolynomial(polynomial, x, y, mPaint)

    //init render format
    val rendFormat = polynomial.renderFormat()


    //get size of final polynomial
    val subSize = mPaint.getPolynomialRangeSize(polynomial, range)

    //get size of range of polynomial cofs with out vertical offset
    val polynomialSize = Pair(subSize.first, subSize.second - mPaint.getPolynomialVerticalOffset())

    //horizontal offset for cof
    var horizontalOffset = 0.0f

    //check for fractions in polynomial
    //need this information for counting vertical offset of variable
    var fractionFlag = false
    range.forEach { if (rendFormat[it].second.containsFractions()) fractionFlag = true }

    //if variable contains any symbol with extra height( obviously it's only '^' symbol)
    //then we must count variable vertical offset in a different way
    var extraHeightFlag = false
    range.forEach { if (rendFormat[it].first.contains('^')) extraHeightFlag = true }

    //check all cofs in range
    for (i in range)
    {
        //cof of polynomial
        val element = rendFormat[i].second
        //size of cof of polynomial
        val cofSize = mPaint.getComplexNumberSize(element)

        //width of variable
        val variableWidth = mPaint.measureText(rendFormat[i].first)

        //vertical offset of each cof is half of diff between overall height and it's personal height
        var cofVerticalOffset =
            ((polynomialSize.second - cofSize.second) / 2) -
                    //if we have fraction in polynomial and this cof isn't fraction then we must count error on height of symbol
                    //for reason of canvas way of counting symbol height
                    if (fractionFlag && !element.containsFractions()) mPaint.getVerticalSpacing() else 0.0f

        //variable contains only en symbols,degree numbers and '^' symbol
        //so we can count it's height by rectangle way
        //but latter we must fix error of degree height type if we have it
        val variableRect = Rect()
        mPaint.getTextBounds(rendFormat[i].first, 0, rendFormat[i].first.length, variableRect)

        val maxOfVariablesHeight = variableRect.height()

        //basic vertical offset for variable
        var variableVerticalOffset =
            (maxOfVariablesHeight + (polynomialSize.second - maxOfVariablesHeight) / 2) + mPaint.getVerticalSpacing()

        //of set for drawing plus
        val plusVerticalOffset = polynomialSize.second / 2

        //fix error in counting of height of symbol
        if (fractionFlag)
        {
            //if we have fraction and we don't have 'i' symbol close than count space between line and number in fraction
            //else we must draw it like 'i' symbol
            variableVerticalOffset -= if (element.isImagination()) mPaint.strokeWidth else 2 * mPaint.getVerticalSpacing()
        }
        else
        {
            //if there is no fractions in polynomial then cof must be rendered higher or variable lower,
            //cof lowering is better for reason of vertical offset in polynomial overall
            if (!extraHeightFlag) cofVerticalOffset -= mPaint.getVerticalSpacing()
        }


        //draw cof
        drawComplex(element, x + horizontalOffset, y + cofVerticalOffset, mPaint)

        //move horizontal pointer to the left of cof
        horizontalOffset += cofSize.first

        //if it's not free position then draw variable
        if (rendFormat[i].first != "")
        {
            drawText(rendFormat[i].first, x + horizontalOffset, y + variableVerticalOffset, mPaint)

            /* increase horizontal position to the left of variable */
            horizontalOffset += variableWidth
        }

        //add some space for smoother render
        horizontalOffset += mPaint.getHorizontalSpacing()

        //if it's not last pait of cof-variable wi must have some sign
        if (range.last != i)
        {
            //if next cof don't have minus sign before cof then we must render plus
            if (!(rendFormat[i + 1].second.isImagination() && rendFormat[i + 1].second.im.isBeloweZero()) &&
                //if cof contain real part then it will be drawn first and we consider it's sign
                !(rendFormat[i + 1].second.isReal() && rendFormat[i + 1].second.re.isBeloweZero())
            )
            {

                drawPlus(x + horizontalOffset, y + plusVerticalOffset, mPaint)

                horizontalOffset += mPaint.getPlusWidth() + mPaint.getHorizontalSpacing()
            }
        }

    }

}

//draws on canvas @this polynomial spreed by ranges @ranges
fun Canvas.drawMultiLinePolynomial(
    polynomial: PolynomialBase,
    x: Float,
    y: Float,
    mPaint: Paint,
    ranges: Array<IntRange>
)
{
    //if empty polynomial we must not render any thing
    if (polynomial == PolynomialBase.EmptyPolynomial || polynomial.degree() == 0) return

    //position of upper angle of line
    var verticalOffset = 0.0f

    //init render format
    val rendFormat = polynomial.renderFormat()

    //get overall size of polynomial
    val overallSize = mPaint.getMultiLinePolynomialSize(polynomial, ranges)

    //draw each line
    for (rangeIndex in ranges.indices)
    {
        //get range representing line
        val range = ranges[rangeIndex]

        //get size of rendering line
        val rangeSize = mPaint.getPolynomialRangeSize(polynomial, range)

        //horizontal offset of line, if line isn't full length of polynomial line max width
        //then it must be rendered at left of polynomial
        val extraOffset =
            (overallSize.first - rangeSize.first - 2 * mPaint.getPlusWidth() - 2 * mPaint.getHorizontalSpacing())

        //check for any error in size counting
        var horizontalOffset = if (extraOffset > 0) extraOffset else 0.0f

        //if it's not fitst line then we must render some sign
        if (rangeIndex != 0)
        {

            //first cof of line
            val nextCof = rendFormat[ranges[rangeIndex].first].second

            //if we haven't any sign in cof, then we must render plus
            if (!(nextCof.isImagination() && nextCof.im.isBeloweZero()) &&
                !(nextCof.isReal() && nextCof.re.isBeloweZero())
            )
            {
                drawPlus(
                    x + horizontalOffset + mPaint.getHorizontalSpacing(),
                    y + verticalOffset + (rangeSize.second / 2), mPaint
                )
                horizontalOffset += mPaint.getPlusWidth() + mPaint.getHorizontalSpacing()
            }
        }

        //draw line of polynomial cofs itself
        drawPolynomialRange(polynomial, x + horizontalOffset, y + verticalOffset, mPaint, range)

        //if it's not last line then we must render some cof
        if (rangeIndex != ranges.size - 1)
        {
            //first cof of next line
            val nextCof = rendFormat[ranges[rangeIndex + 1].first].second

            //if fist cof of nex line isn't countains minus in it we must draw plus, else minus
            if (!(nextCof.isImagination() && nextCof.im.isBeloweZero()) &&
                !(nextCof.isReal() && nextCof.re.isBeloweZero())
            )
            {
                drawPlus(
                    x + horizontalOffset + (rangeSize.first),
                    y + verticalOffset + ((rangeSize.second - mPaint.getBaseHeight() / 2) / 2),
                    mPaint
                )
            }
            else
            {
                drawMinus(
                    x + horizontalOffset + (rangeSize.first),
                    y + verticalOffset + ((rangeSize.second - mPaint.getBaseHeight() / 2) / 2),
                    mPaint
                )
            }
        }

        //increase vertical pos for next line
        verticalOffset += rangeSize.second
    }

}

//wrapper around drawing dots for polynomial
fun Canvas.drawPolynomialAsDots(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint)
{
    if (polynomial == PolynomialBase.EmptyPolynomial || polynomial.degree() == 0) return

    drawProporsionalDotsInBrackets(5, x, y, mPaint)

}

//draw equation that setted by polynomial @polynomial on canvas
fun Canvas.drawEquation(polynomial: PolynomialBase, x: Float, y: Float, mPaint: Paint)
{
    //draw polynomial
    val polynomialSize = mPaint.getPolynomialSize(polynomial)
    this.drawPolynomial(polynomial, x, y, mPaint)

    //draw equals and zero after polynomial to get equation
    val verticalOffset =
        mPaint.getBaseHeight() + (polynomialSize.second - mPaint.getBaseHeight()) / 2

    this.drawText(
        "=0",
        x + polynomialSize.first + mPaint.getHorizontalSpacing(),
        y + verticalOffset,
        mPaint
    )
}

//todo:: rewrite to smoother render[ probalby mast adapte polynomial roots to polynomial render format]
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
