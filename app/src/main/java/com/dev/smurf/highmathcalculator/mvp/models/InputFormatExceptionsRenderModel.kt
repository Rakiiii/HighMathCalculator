package com.dev.smurf.highmathcalculator.mvp.models

import android.graphics.*
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.PaintExtension.getVerticalSpacing
import com.dev.smurf.highmathcalculator.StringsExtension.fields
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

//todo::add extra function layer for less boilerplate in matrix render [must get only 2 functions]
//todo:: implement single responsibility principal
//todo:: add some comments


@ExperimentalCoroutinesApi
class InputFormatExceptionsRenderModel
{

    var screenWidth = 0f
    var screenHeight = 0f

    var inputFormWidth = 0.0f
    var inputFromHeight = 0.0f


    private val otherElemsHeight = 100

    fun getErrorDialogWidth() = inputFormWidth - getWidthMargin()
    fun getErrorDialogHeight() = inputFromHeight
    private fun getHeightMargin() = inputFromHeight / 20
    private fun getWidthMargin() = inputFormWidth / 20
    private fun getMaxBitmapWidth() = getErrorDialogWidth() - 2 * getWidthMargin()
    private fun getMaxBitmapHeight() =
        getErrorDialogHeight() - 2 * getHeightMargin() - otherElemsHeight


    private val bitmapConfig = Bitmap.Config.ARGB_8888
    private val widthOfEmptyMatrix = 15

    @ExperimentalCoroutinesApi
    suspend fun drawErroredMatrixWhenPartOfLineIsWrong(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String
    ): Bitmap = withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

        return@withContext drawErroredText(
            coroutineScope = coroutineScope,
            input = input,
            wrongLine = wrongLine,
            getSize = { s, s2, paint, paint2 ->
                getSizeOfLineWithWrongSubstring(
                    coroutineScope = coroutineScope,
                    line = s,
                    wrongLine = s2,
                    defaultPainter = paint,
                    errorPainter = paint2
                )
            },
            separate = { s, s2, paint, paint2, suspendFunction4 ->
                withContext(Dispatchers.Default) {
                    spreadMatrixOnLines(
                        s
                    )
                }
            },
            getSeparatedSize = { mutableList, s, paint, paint2, suspendFunction4 ->
                getSizeOfLines(
                    lines = mutableList,
                    wrongPart = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    getSize = suspendFunction4
                )
            },
            drawLines = { mutableList, s, paint, paint2 ->
                drawLines(
                    coroutineScope = coroutineScope,
                    lines = mutableList,
                    wrongLine = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    drawLine = { str1: String, str2: String, p1: Paint, p2: Paint ->
                        drawLineWithWrongSubstring(
                            coroutineScope = coroutineScope,
                            line = str1,
                            wrongLine = str2,
                            defaultPainter = p1,
                            errorPainter = p2
                        )
                    }
                )
            }
        )
    }

    @ExperimentalCoroutinesApi
    suspend fun drawErroredMatrixWhenFullLineIsWrong(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String
    ): Bitmap = withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

        return@withContext drawErroredText(
            coroutineScope = coroutineScope,
            input = input,
            wrongLine = wrongLine,
            getSize = { s, s2, paint, paint2 ->
                getSizeOfProbablyWrongLine(
                    coroutineScope = coroutineScope,
                    line = s,
                    wrongLine = s2,
                    defaultPainter = paint,
                    errorPainter = paint2
                )
            },
            separate = { s, s2, paint, paint2, suspendFunction4 ->
                withContext(Dispatchers.Default) {
                    spreadMatrixOnLines(
                        s
                    )
                }
            },
            getSeparatedSize = { mutableList, s, paint, paint2, suspendFunction4 ->
                getSizeOfLines(
                    lines = mutableList,
                    wrongPart = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    getSize = suspendFunction4
                )
            },
            drawLines = { mutableList, s, paint, paint2 ->
                drawLines(
                    coroutineScope = coroutineScope,
                    lines = mutableList,
                    wrongLine = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    drawLine = { str1: String, str2: String, p1: Paint, p2: Paint ->
                        drawProbablyWrongLine(
                            coroutineScope = coroutineScope,
                            line = str1,
                            wrongLine = str2,
                            defaultPainter = p1,
                            errorPainter = p2
                        )
                    }
                )
            }
        )
    }


    suspend fun drawErroredMatrixWithWrongChars(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String
    ): Bitmap = withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

        return@withContext drawErroredText(
            coroutineScope = coroutineScope,
            input = input,
            wrongLine = wrongLine,
            getSize = { s, s2, paint, paint2 ->
                getSizeOfLineWithWrongChars(
                    coroutineScope = coroutineScope,
                    line = s,
                    wrongChars = s2,
                    defaultPainter = paint,
                    errorPainter = paint2
                )
            },
            separate = { s, s2, paint, paint2, suspendFunction4 ->
                withContext(Dispatchers.Default) {
                    spreadMatrixOnLines(
                        s
                    )
                }
            },
            getSeparatedSize = { mutableList, s, paint, paint2, suspendFunction4 ->
                getSizeOfLines(
                    lines = mutableList,
                    wrongPart = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    getSize = suspendFunction4
                )
            },
            drawLines = { mutableList, s, paint, paint2 ->
                drawLines(
                    coroutineScope = coroutineScope,
                    lines = mutableList,
                    wrongLine = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    drawLine = { str1: String, str2: String, p1: Paint, p2: Paint ->
                        drawLineWithWrongChars(
                            coroutineScope = coroutineScope,
                            line = str1,
                            wrongChars = str2,
                            defaultPainter = p1,
                            errorPainter = p2
                        )
                    }
                )
            }
        )
    }


    //draw line with wrong substring
    suspend fun drawLineWithWrongSubstring(
        coroutineScope: CoroutineScope,
        defaultPainter: Paint,
        errorPainter: Paint,
        line: String,
        wrongLine: String
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

            //if empty act like empty
            if (line == "") return@withContext drawEmptyLine(errorPainter)

            //if no wrong substring draw like notmal line
            if (!line.contains(wrongLine, true)) return@withContext drawLine(
                line,
                defaultPainter
            )

            //get size for bitmap
            val size: Pair<Float, Float> = getSizeOfLineWithWrongSubstring(
                coroutineScope,
                defaultPainter,
                errorPainter,
                line,
                wrongLine
            )

            //spread start line on 3 substrings : before wrong, wrong, after wrong
            val wrongLineStart = line.indexOf(wrongLine, 0, true)

            //before wrong
            val firstPart = line.substring(0, wrongLineStart)


            //after wrong
            val secondPart = line.substring(wrongLineStart + wrongLine.length)

            //bitmap with error line
            val errorBitmap =
                Bitmap.createBitmap(size.first.toInt(), size.second.toInt(), bitmapConfig)

            val canvas = Canvas(errorBitmap)

            var horizontalOffset = 0.0f

            //draw non wrong
            if (firstPart.isNotEmpty())
            {
                val rect = Rect()
                defaultPainter.getTextBounds(firstPart, 0, firstPart.length, rect)

                canvas.drawText(
                    firstPart,
                    0,
                    firstPart.length,
                    horizontalOffset,
                    rect.height().toFloat(),
                    defaultPainter
                )
                horizontalOffset += defaultPainter.measureText(firstPart)
            }

            //draw wrong
            if (wrongLine.isNotEmpty())
            {
                val rect = Rect()
                errorPainter.getTextBounds(wrongLine, 0, wrongLine.length, rect)

                canvas.drawText(
                    wrongLine,
                    0,
                    wrongLine.length,
                    horizontalOffset,
                    rect.height().toFloat(),
                    errorPainter
                )

                horizontalOffset += errorPainter.measureText(wrongLine)

            }

            //draw non wrong
            if (secondPart.isNotEmpty())
            {
                val rect = Rect()
                defaultPainter.getTextBounds(secondPart, 0, secondPart.length, rect)

                canvas.drawText(
                    secondPart,
                    0,
                    secondPart.length,
                    horizontalOffset,
                    rect.height().toFloat(),
                    defaultPainter
                )
            }


            return@withContext errorBitmap
        }

    }

    private fun drawLine(line: String, defaultPainter: Paint): Bitmap
    {
        val size = getSizeOfLine(defaultPainter, line)
        val bitmap = Bitmap.createBitmap(size.first.toInt(), size.second.toInt(), bitmapConfig)

        Canvas(bitmap).drawText(line, 0, line.length, 0.0f, size.second, defaultPainter)

        return bitmap
    }


    //returns size  of line which can containt wrong part
    private suspend fun getSizeOfLineWithWrongSubstring(
        coroutineScope: CoroutineScope,
        defaultPainter: Paint,
        errorPainter: Paint,
        line: String,
        wrongLine: String
    ): Pair<Float, Float>
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

            //if empty act like with empty line
            if (line == "") return@withContext getSizeOfEmptyMatrix(errorPainter)

            //if line does not contain wrong substring calc it like normal line
            //needed for better performance
            if (!line.contains(wrongLine, true)) return@withContext getSizeOfLine(
                defaultPainter = defaultPainter,
                input = line
            )

            //spread start line on 3 substrings : before wrong, wrong, after wrong
            val wrongLineStart = line.indexOf(wrongLine, 0, true)

            //before wrong
            val firstPart = line.substring(0, wrongLineStart)

            //after wrong
            val secondPart = line.substring(wrongLineStart + wrongLine.length)

            var overallWidth = 0.0f

            var maxHeight = 0.0f

            //calc non wrong part
            if (firstPart.isNotEmpty())
            {
                val rectSize = Rect()

                defaultPainter.getTextBounds(firstPart, 0, firstPart.length, rectSize)

                maxHeight = if (rectSize.height().toFloat() > maxHeight) rectSize.height().toFloat()
                else maxHeight

                val width = defaultPainter.measureText(firstPart)

                overallWidth += width
            }

            //calc wrong part
            if (wrongLine.isNotEmpty())
            {
                val rectSize = Rect()

                errorPainter.getTextBounds(wrongLine, 0, wrongLine.length, rectSize)

                maxHeight = if (rectSize.height().toFloat() > maxHeight) rectSize.height().toFloat()
                else maxHeight

                val width = errorPainter.measureText(wrongLine)

                overallWidth += width
            }

            //calc non wrong part
            if (secondPart.isNotEmpty())
            {
                val rectSize = Rect()

                defaultPainter.getTextBounds(secondPart, 0, secondPart.length, rectSize)

                maxHeight = if (rectSize.height().toFloat() > maxHeight) rectSize.height().toFloat()
                else maxHeight

                val width = defaultPainter.measureText(secondPart)

                overallWidth += width
            }

            Pair(overallWidth, maxHeight + defaultPainter.getVerticalSpacing())
        }
    }

    //returns size of default line
    private fun getSizeOfLine(defaultPainter: Paint, input: String): Pair<Float, Float>
    {
        val rect = Rect()
        defaultPainter.getTextBounds(input, 0, input.length, rect)

        val width = defaultPainter.measureText(input)

        return Pair(width, rect.height().toFloat() + defaultPainter.getVerticalSpacing())
    }

    //spread matrix on line
    private fun spreadMatrixOnLines(input: String): MutableList<String>
    {
        val subRes = input.fields("\n")

        //need this for better ux
        val finalRes =
            MutableList(subRes.size) { pos -> subRes[pos].trim { s -> s == ' ' || s == '\n' } }

        return finalRes
    }


    private suspend fun getSizeOfProbablyWrongLine(
        coroutineScope: CoroutineScope,
        line: String,
        wrongLine: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Pair<Float, Float>
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            if (line != "")
            {
                if (line == wrongLine) getSizeOfLine(errorPainter, line)
                else getSizeOfLine(defaultPainter, line)
            }
            else getSizeOfEmptyMatrix(errorPainter)
        }
    }

    //draw line that can be wrong
    //line is wrong when it is same as wrong
    private suspend fun drawProbablyWrongLine(
        coroutineScope: CoroutineScope,
        line: String,
        wrongLine: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            if (line != "")
            {
                if (line == wrongLine)
                {
                    drawLine(line, errorPainter)
                }
                else drawLine(line, defaultPainter)
            }
            else drawEmptyLine(errorPainter)
        }
    }

    //lets draw empty line as oval
    private fun drawEmptyLine(errorPainter: Paint): Bitmap
    {
        val rect = Rect()
        errorPainter.getTextBounds("1", 0, 1, rect)

        val bitmap = Bitmap.createBitmap(widthOfEmptyMatrix, rect.height(), bitmapConfig)

        Canvas(bitmap).drawOval(
            0.0f,
            0.0f,
            bitmap.width.toFloat(),
            bitmap.height.toFloat(),
            errorPainter
        )

        return bitmap
    }

    //lets draw empty line as oval
    private fun getSizeOfEmptyMatrix(errorPainter: Paint): Pair<Float, Float>
    {
        val rect = Rect()
        errorPainter.getTextBounds("1", 0, 1, rect)

        return Pair(widthOfEmptyMatrix.toFloat(), rect.height().toFloat())
    }

    //returns size of line with wrongs chars
    private suspend fun getSizeOfLineWithWrongChars(
        coroutineScope: CoroutineScope,
        line: String,
        wrongChars: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Pair<Float, Float>
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

            //if line is empty must have size of empty line
            if (line == "") return@withContext getSizeOfEmptyMatrix(errorPainter)

            //if matrix does  not contain wrong chars draw as regular line
            //needed for bbetter perfomance
            if (line.filter { s -> wrongChars.contains(s) } == "") return@withContext getSizeOfLine(
                defaultPainter = defaultPainter,
                input = line
            )
            var width = 0.0f
            var height = 0.0f

            //count each symbol size
            for (i in line)
            {
                if (wrongChars.contains(i, true))
                {
                    width += errorPainter.measureText(i.toString())
                    val rect = Rect()
                    errorPainter.getTextBounds(i.toString(), 0, 1, rect)
                    height =
                        if (rect.height().toFloat() > height) rect.height().toFloat() else height
                }
                else
                {
                    width += defaultPainter.measureText(i.toString())
                    val rect = Rect()
                    defaultPainter.getTextBounds(i.toString(), 0, 1, rect)
                    height =
                        if (rect.height().toFloat() > height) rect.height().toFloat() else height
                }
            }

            return@withContext Pair(
                width,
                height + errorPainter.fontMetrics.descent - errorPainter.fontMetrics.ascent
            )
        }
    }

    //draw line when some chars are wrong
    private suspend fun drawLineWithWrongChars(
        coroutineScope: CoroutineScope,
        line: String,
        wrongChars: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            //if line is emty draw vertical line
            if (line == "") return@withContext drawEmptyLine(errorPainter)

            //if linw does not contain wrong chars draw it like normal line
            //needed for better performance
            if (line.filter { s -> wrongChars.contains(s) } == "") return@withContext drawLine(
                line,
                defaultPainter
            )

            //size of line
            val size = getSizeOfLineWithWrongChars(
                coroutineScope,
                line,
                wrongChars,
                defaultPainter,
                errorPainter
            )

            //creates bitmap for line
            val bitmap = Bitmap.createBitmap(size.first.toInt(), size.second.toInt(), bitmapConfig)

            val canvas = Canvas(bitmap)

            var horizontalOffset = 0.0f

            //draw each symbol
            for (i in line)
            {
                //if we draw wrong chars
                if (wrongChars.contains(i))
                {
                    val rect = Rect()
                    errorPainter.getTextBounds(i.toString(), 0, 1, rect)

                    canvas.drawText(
                        i.toString(),
                        0,
                        1,
                        horizontalOffset,
                        rect.height().toFloat() + errorPainter.fontMetrics.descent,
                        errorPainter
                    )

                    horizontalOffset += errorPainter.measureText(i.toString())
                }
                else
                {
                    //if we draw non wrong chars
                    val rect = Rect()
                    defaultPainter.getTextBounds(i.toString(), 0, 1, rect)

                    val extraVerticalOffset = (size.second - rect.height().toFloat()) / 2

                    canvas.drawText(
                        i.toString(),
                        0,
                        1,
                        horizontalOffset,
                        rect.height().toFloat() + extraVerticalOffset,
                        defaultPainter
                    )

                    horizontalOffset += defaultPainter.measureText(i.toString())
                }
            }

            return@withContext bitmap
        }
    }


    /*
     *   Polynomials
     *
     * Render functions for polynomials input errors
     *
     */

    //amount of symbols that will be removed from line
    //bigger for faster render smaller for smoother render
    private val stepSize = 10

    //return line separation that fit screen size
    private suspend fun getPossibleLineSeparationsForPolynomial(
        input: String,
        wrongPart: String,
        defaultPainter: Paint,
        errorPainter: Paint,
        getSize: suspend (String, String, Paint, Paint) -> Float
    ): MutableList<String> = withContext(Dispatchers.Default) {

        val separation = ArrayList<String>()

        //if polynomial can be render in one line return as one line
        if (getSize(
                input,
                wrongPart,
                defaultPainter,
                errorPainter
            ) < inputFormWidth
        ) return@withContext arrayListOf(input)

        //in other case recursively break line
        var subString = input

        while (subString.isNotEmpty())
        {
            //width of substring
            val subWidth = getSize(subString, wrongPart, defaultPainter, errorPainter)

            //if substring is fited screen then stop
            if (subWidth < inputFormWidth)
            {
                separation.add(subString)
                return@withContext separation
            }

            //count max length of possible line
            val maxSymbolsLength = (subString.length * (inputFormWidth / subWidth)).toInt()

            //get line from substring
            var line = subString.substring(0, maxSymbolsLength)
            //count line width
            var lineWidth = getSize(line, wrongPart, defaultPainter, errorPainter)

            //while line is not fiting the screen recursively remove symbols
            while (lineWidth >= inputFormWidth)
            {
                line = line.substring(0, line.length - stepSize)
                lineWidth = getSize(line, wrongPart, defaultPainter, errorPainter)
            }

            //remove line from substring
            subString = subString.substring(line.length)
            separation.add(line)
        }
        return@withContext separation
    }

    //return flow of bitmap with drawed line
    private suspend fun drawLines(
        coroutineScope: CoroutineScope,
        lines: MutableList<String>,
        wrongLine: String,
        defaultPainter: Paint,
        errorPainter: Paint,
        drawLine: suspend (String, String, Paint, Paint) -> Bitmap
    ): Flow<Bitmap> = flow {
        lines.map {
            emit(drawLine(it, wrongLine, defaultPainter, errorPainter))
        }
    }.flowOn(Dispatchers.Default)


    //returns flow of sizes of lines
    private suspend fun getSizeOfLines(
        lines: MutableList<String>,
        wrongPart: String,
        defaultPainter: Paint,
        errorPainter: Paint,
        getSize: suspend (String, String, Paint, Paint) -> Pair<Float, Float>
    ): Flow<Pair<Float, Float>> = flow {
        lines.map {
            emit(getSize(it, wrongPart, defaultPainter, errorPainter))
        }
    }.flowOn(Dispatchers.Default)


    /*
     * abstract render strategy algorithm
     * 1.find text size which can fit window
     * 2.separate text on lines with this painter
     * 3.count offsets etc...
     * 4.draw each line async and concat each line bitmap to one ultraMegaMultyLine bitmap
*/
    private suspend fun drawErroredText(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String,
        getSize: suspend (String, String, Paint, Paint) -> Pair<Float, Float>,
        separate: suspend (String, String, Paint, Paint, suspend (String, String, Paint, Paint) -> Float) -> MutableList<String>,
        getSeparatedSize: suspend (MutableList<String>, String, Paint, Paint, suspend (String, String, Paint, Paint) -> Pair<Float, Float>) -> Flow<Pair<Float, Float>>,
        drawLines: suspend (MutableList<String>, String, Paint, Paint) -> Flow<Bitmap>
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            val defaultPainter = CanvasRenderSpecification.createBlackPainter()
            val errorPainter = CanvasRenderSpecification.createRedPainterWithUnderline()

            defaultPainter.textSize += 20
            errorPainter.textSize += 20


            var size: Pair<Float, Float>
            var lines: MutableList<String>

            //finde fited text size
            do
            {
                defaultPainter.textSize -= 5
                errorPainter.textSize -= 5
                //separate last one
                lines = separate(
                    input,
                    wrongLine,
                    defaultPainter,
                    errorPainter
                ) { str1, str2, p1, p2 -> getSize(str1, str2, p1, p2).first }

                var width = 0.0f
                var height = 0.0f

                val sizeFlow = getSeparatedSize(
                    lines,
                    wrongLine,
                    defaultPainter,
                    errorPainter
                ) { str1, str2, p1, p2 -> getSize(str1, str2, p1, p2) }

                sizeFlow.collect { s ->
                    width = if (s.first > width) s.first else width; height += s.second
                }

                size = Pair(width, height)
            } while (size.first > getMaxBitmapWidth() || size.second > getMaxBitmapHeight())

            //count all offset
            val leftFloat = getWidthMargin()
            var topFloat = getHeightMargin()

            //ultraMegaMultyLine bitmap
            val finalBitmap = Bitmap.createBitmap(
                (size.first + leftFloat * 2).toInt(),
                (size.second + topFloat * 2).toInt(),
                bitmapConfig
            )

            val canvas = Canvas(finalBitmap)

            val lineFlow = drawLines(lines, wrongLine, defaultPainter, errorPainter)

            //concat each line bitmap to one
            lineFlow.collect { lineBitmap ->
                canvas.drawBitmap(
                    lineBitmap,
                    leftFloat,
                    topFloat,
                    defaultPainter
                );topFloat += lineBitmap.height
            }

            return@withContext finalBitmap
        }
    }


    suspend fun drawErroredPolynomialWithWrongChars(
        coroutineScope: CoroutineScope,
        input: String,
        wrongChars: String
    ): Bitmap = withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

        return@withContext drawErroredText(
            coroutineScope = coroutineScope,
            input = input,
            wrongLine = wrongChars,
            getSize = { str1, str2, p1, p2 ->
                getSizeOfLineWithWrongChars(
                    coroutineScope = coroutineScope,
                    line = str1,
                    wrongChars = str2,
                    defaultPainter = p1,
                    errorPainter = p2
                )
            },
            separate = { str1, str2, p1, p2, f1 ->
                getPossibleLineSeparationsForPolynomial(
                    input = str1,
                    wrongPart = str2,
                    defaultPainter = p1,
                    errorPainter = p2,
                    getSize = f1
                )
            },
            getSeparatedSize = { list, str, p1, p2, f ->
                getSizeOfLines(
                    list,
                    str,
                    p1,
                    p2,
                    f
                )
            },
            drawLines = { list, str, p1, p2 ->
                drawLines(
                    coroutineScope = coroutineScope,
                    lines = list,
                    wrongLine = str,
                    defaultPainter = p1,
                    errorPainter = p2,
                    drawLine = { s, s2, paint, paint2 ->
                        drawLineWithWrongChars(
                            coroutineScope = coroutineScope,
                            line = s,
                            wrongChars = s2,
                            defaultPainter = paint,
                            errorPainter = paint2
                        )
                    }
                )
            }
        )
    }

    suspend fun drawErroredPolynomialWithWrongSubstring(
        coroutineScope: CoroutineScope,
        input: String,
        wrongSubstring: String
    ): Bitmap = withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

        return@withContext drawErroredText(
            coroutineScope = coroutineScope,
            input = input,
            wrongLine = wrongSubstring,
            getSize = { str1, str2, p1, p2 ->
                getSizeOfLineWithWrongSubstring(
                    coroutineScope = coroutineScope,
                    line = str1,
                    wrongLine = str2,
                    defaultPainter = p1,
                    errorPainter = p2
                )
            },
            separate = { s1: String,
                         s2: String,
                         paint1: Paint,
                         paint2: Paint,
                         suspendFunction4: suspend (String, String, Paint, Paint) -> Float ->
                getPossibleLineSeparationsForPolynomial(
                    input = s1,
                    wrongPart = s2,
                    defaultPainter = paint1,
                    errorPainter = paint2,
                    getSize = suspendFunction4
                )
            },
            getSeparatedSize = { mutableList, s, paint, paint2, suspendFunction4 ->
                getSizeOfLines(
                    lines = mutableList,
                    wrongPart = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    getSize = suspendFunction4
                )
            },
            drawLines = { mutableList, s, paint, paint2 ->
                drawLines(coroutineScope = coroutineScope,
                    lines = mutableList,
                    wrongLine = s,
                    defaultPainter = paint,
                    errorPainter = paint2,
                    drawLine = { str1, str2, p1, p2 ->
                        drawLineWithWrongSubstring(
                            coroutineScope = coroutineScope,
                            line = str1,
                            wrongLine = str2,
                            defaultPainter = p1,
                            errorPainter = p2
                        )
                    }
                )
            }
        )
    }

}