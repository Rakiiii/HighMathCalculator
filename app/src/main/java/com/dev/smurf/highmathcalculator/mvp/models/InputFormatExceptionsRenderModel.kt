package com.dev.smurf.highmathcalculator.mvp.models

import android.graphics.*
import com.dev.smurf.highmathcalculator.CanvasExtension.CanvasRenderSpecification
import com.dev.smurf.highmathcalculator.PaintExtension.getVerticalSpacing
import com.dev.smurf.highmathcalculator.StringsExtension.fields
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

//todo:: move from cycles to map
//todo::move to strategy pattern
//todo:: implement single responsibility principal


@ExperimentalCoroutinesApi
class InputFormatExceptionsRenderModel
{

    var screenWidth = 0.0f
    var screenHeight = 0.0f
    fun getHeightMargin() = screenHeight / 20
    fun getWidthMargin() = screenWidth / 20

    private val bitmapConfig = Bitmap.Config.ARGB_8888
    private val widthOfEmptyMatrix = 15

    @ExperimentalCoroutinesApi
    suspend fun drawErroredMatrixWhenPartOfLineIsWrong(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            val defaultPainter = CanvasRenderSpecification.createBlackPainter()
            val errorPainter = CanvasRenderSpecification.createRedPainterWithUnderline()

            defaultPainter.textSize += 20
            errorPainter.textSize += 20

            var size: Pair<Float, Float>

            do
            {
                defaultPainter.textSize -= 5
                errorPainter.textSize -= 5

                var width = 0.0f
                var height = 0.0f
                getMatrixLinesSizesWithWrongSubstring(
                    coroutineScope,
                    input,
                    wrongLine,
                    defaultPainter,
                    errorPainter
                ).collect { lineSize ->
                    width =
                        if (lineSize.first > width) lineSize.first else width; height += lineSize.second
                }

                size = Pair(width, height)
            } while (size.first > screenWidth || size.second > screenHeight)

            val leftFloat = getWidthMargin()
            var topFloat = getHeightMargin()

            val finalBitmap = Bitmap.createBitmap(
                (size.first + leftFloat * 2).toInt(),
                (size.second + topFloat * 2).toInt(),
                bitmapConfig
            )

            val canvas = Canvas(finalBitmap)

            val lineFlow = drawMatrixLinesWithWrongSubstring(
                coroutineScope,
                input,
                wrongLine,
                defaultPainter,
                errorPainter
            )

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

    @ExperimentalCoroutinesApi
    suspend fun drawErroredMatrixWhenFullLineIsWrong(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            val defaultPainter = CanvasRenderSpecification.createBlackPainter()
            val errorPainter = CanvasRenderSpecification.createRedPainterWithUnderline()

            defaultPainter.textSize += 20
            errorPainter.textSize += 20

            var size: Pair<Float, Float>

            do
            {
                defaultPainter.textSize -= 5
                errorPainter.textSize -= 5

                var width = 0.0f
                var height = 0.0f
                getSizeOfMatrixLinesWithWrongLine(
                    input,
                    wrongLine,
                    defaultPainter,
                    errorPainter
                ).collect { lineSize ->
                    width =
                        if (lineSize.first > width) lineSize.first else width; height += lineSize.second
                }

                size = Pair(width, height)
            } while (size.first > screenWidth || size.second > screenHeight)

            val leftFloat = getWidthMargin()
            var topFloat = getHeightMargin()

            val finalBitmap = Bitmap.createBitmap(
                (size.first + leftFloat * 2).toInt(),
                (size.second + topFloat * 2).toInt(),
                bitmapConfig
            )

            val canvas = Canvas(finalBitmap)

            val bitmapFlow =
                drawMatrixLinesWithWrongLine(input, wrongLine, defaultPainter, errorPainter)
            bitmapFlow.collect { lineBitmap ->
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

    suspend fun drawErroredMatrixWithWrongChars(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            val defaultPainter = CanvasRenderSpecification.createBlackPainter()
            val errorPainter = CanvasRenderSpecification.createRedPainterWithUnderline()

            defaultPainter.textSize += 20
            errorPainter.textSize += 20

            var size: Pair<Float, Float>

            do
            {
                defaultPainter.textSize -= 5
                errorPainter.textSize -= 5

                var width = 0.0f
                var height = errorPainter.getVerticalSpacing()
                getSizeOfMatrixLinesWithWrongChars(
                    coroutineScope,
                    input,
                    wrongLine,
                    defaultPainter,
                    errorPainter
                ).collect { lineSize ->
                    width =
                        if (lineSize.first > width) lineSize.first else width; height += lineSize.second + errorPainter.getVerticalSpacing()
                }

                size = Pair(width, height)
            } while (size.first > screenWidth || size.second > screenHeight)

            val leftFloat = getWidthMargin()
            var topFloat = getHeightMargin()

            val finalBitmap = Bitmap.createBitmap(
                (size.first + leftFloat * 2).toInt(),
                (size.second + topFloat * 2).toInt(),
                bitmapConfig
            )

            val canvas = Canvas(finalBitmap)

            val bitmapFlow = drawMatrixLinesWithWrongChars(
                coroutineScope, input, wrongLine, defaultPainter, errorPainter
            )

            bitmapFlow.collect { lineBitmap ->
                canvas.drawBitmap(
                    lineBitmap,
                    leftFloat,
                    topFloat,
                    defaultPainter
                );topFloat += lineBitmap.height + errorPainter.getVerticalSpacing()
            }

            return@withContext finalBitmap
        }
    }


    suspend fun drawWrongLineOfInputMatrix(
        coroutineScope: CoroutineScope,
        defaultPainter: Paint,
        errorPainter: Paint,
        input: String,
        wrongLine: String
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {

            val size: Pair<Float, Float> = getSizeOfLineWithWrongPart(
                coroutineScope,
                defaultPainter,
                errorPainter,
                input,
                wrongLine
            )


            val wrongLineStart = input.indexOf(wrongLine, 0, true)

            val firstPart = input.substring(0, wrongLineStart)

            val secondPart = input.substring(wrongLineStart + wrongLine.length)


            val errorBitmap =
                Bitmap.createBitmap(size.first.toInt(), size.second.toInt(), bitmapConfig)

            val canvas = Canvas(errorBitmap)

            var horizontalOffset = 0.0f

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

    fun drawMatrixLine(line: String, defaultPainter: Paint): Bitmap
    {
        val size = getSizeOfMatrixLine(defaultPainter, line)
        val bitmap = Bitmap.createBitmap(size.first.toInt(), size.second.toInt(), bitmapConfig)

        Canvas(bitmap).drawText(line, 0, line.length, 0.0f, size.second, defaultPainter)

        return bitmap
    }


    suspend fun getSizeOfLineWithWrongPart(
        coroutineScope: CoroutineScope,
        defaultPainter: Paint,
        errorPainter: Paint,
        input: String,
        wrongLine: String
    ): Pair<Float, Float>
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            val wrongLineStart = input.indexOf(wrongLine, 0, true)

            val firstPart = input.substring(0, wrongLineStart)

            val secondPart = input.substring(wrongLineStart + wrongLine.length)

            var overallWidth = 0.0f

            var maxHeight = 0.0f

            if (firstPart.isNotEmpty())
            {
                val rectSize = Rect()

                defaultPainter.getTextBounds(firstPart, 0, firstPart.length, rectSize)

                maxHeight = if (rectSize.height().toFloat() > maxHeight) rectSize.height().toFloat()
                else maxHeight

                val width = defaultPainter.measureText(firstPart)

                overallWidth += width
            }

            if (wrongLine.isNotEmpty())
            {
                val rectSize = Rect()

                errorPainter.getTextBounds(wrongLine, 0, wrongLine.length, rectSize)

                maxHeight = if (rectSize.height().toFloat() > maxHeight) rectSize.height().toFloat()
                else maxHeight

                val width = errorPainter.measureText(wrongLine)

                overallWidth += width
            }

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

    fun getSizeOfMatrixLine(defaultPainter: Paint, input: String): Pair<Float, Float>
    {
        val rect = Rect()
        defaultPainter.getTextBounds(input, 0, input.length, rect)

        val width = defaultPainter.measureText(input)

        return Pair(width, rect.height().toFloat() + defaultPainter.getVerticalSpacing())
    }

    fun spreadMatrixOnLines(input: String) = input.fields("\n")

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun getMatrixLinesSizesWithWrongSubstring(
        coroutineScope: CoroutineScope,
        input: String,
        wrongLine: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Flow<Pair<Float, Float>> = flow {
        val lines = spreadMatrixOnLines(input)
        for (line in lines)
        {
            if (line.contains(wrongLine, true)) emit(
                getSizeOfLineWithWrongPart(
                    coroutineScope,
                    defaultPainter,
                    errorPainter,
                    input,
                    wrongLine
                )
            )
            else emit(getSizeOfMatrixLine(defaultPainter, input))
        }
    }.flowOn(Dispatchers.Default)

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    fun drawMatrixLinesWithWrongSubstring(
        coroutineScope: CoroutineScope,
        input: String, wrongLine: String, defaultPainter: Paint, errorPainter: Paint
    ): Flow<Bitmap> = flow {
        val lines = spreadMatrixOnLines(input)
        for (line in lines)
        {
            if (line.contains(wrongLine, true)) emit(
                drawWrongLineOfInputMatrix(
                    coroutineScope,
                    defaultPainter,
                    errorPainter,
                    line,
                    wrongLine
                )
            )
            else emit(drawMatrixLine(line, defaultPainter))
        }
    }.flowOn(Dispatchers.Default)


    @ExperimentalCoroutinesApi
    fun getSizeOfMatrixLinesWithWrongLine(
        input: String,
        wrongLine: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Flow<Pair<Float, Float>> = flow {
        val lines = spreadMatrixOnLines(input)

        for (line in lines)
        {
            if(line != "")
            {
                if (line == wrongLine) emit(getSizeOfMatrixLine(errorPainter, line))
                else emit(getSizeOfMatrixLine(defaultPainter, line))
            }else emit(getSizeOfEmptyMatrix(errorPainter))
        }
    }.flowOn(Dispatchers.Default)

    fun drawMatrixLinesWithWrongLine(
        input: String,
        wrongLine: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Flow<Bitmap> = flow {
        val lines = spreadMatrixOnLines(input)

        for (line in lines)
        {
            if (line != "")
            {
                if (line == wrongLine)
                {
                    emit(drawMatrixLine(line, errorPainter))
                }
                else emit(drawMatrixLine(line, defaultPainter))
            }
            else emit(drawEmptyLine(errorPainter))
        }
    }

    fun drawEmptyLine(errorPainter: Paint): Bitmap
    {
        val rect = Rect()
        errorPainter.getTextBounds("1", 0, 1, rect)

        val bitmap = Bitmap.createBitmap(widthOfEmptyMatrix, rect.height(), bitmapConfig)

        bitmap.eraseColor(errorPainter.color)
        return bitmap
    }

    fun getSizeOfEmptyMatrix(errorPainter: Paint):Pair<Float,Float>
    {
        val rect = Rect()
        errorPainter.getTextBounds("1", 0, 1, rect)

        return Pair(widthOfEmptyMatrix.toFloat(),rect.height().toFloat())
    }

    suspend fun getSizeOfLineWithWrongChars(
        coroutineScope: CoroutineScope,
        line: String,
        wrongChars: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Pair<Float, Float>
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            var width = 0.0f
            var height = 0.0f

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

            return@withContext Pair(width, height + errorPainter.fontMetrics.descent)
        }
    }

    suspend fun drawLineWithWrongChars(
        coroutineScope: CoroutineScope,
        line: String,
        wrongChars: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Bitmap
    {
        return withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
            val size = getSizeOfLineWithWrongChars(
                coroutineScope,
                line,
                wrongChars,
                defaultPainter,
                errorPainter
            )

            val bitmap = Bitmap.createBitmap(size.first.toInt(), size.second.toInt(), bitmapConfig)

            val canvas = Canvas(bitmap)

            var horizontalOffset = 0.0f

            for (i in line)
            {
                if (wrongChars.contains(i))
                {
                    val rect = Rect()
                    errorPainter.getTextBounds(i.toString(), 0, 1, rect)

                    canvas.drawText(
                        i.toString(),
                        0,
                        1,
                        horizontalOffset,
                        rect.height().toFloat(),
                        errorPainter
                    )

                    horizontalOffset += errorPainter.measureText(i.toString())
                }
                else
                {
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


    fun getSizeOfMatrixLinesWithWrongChars(
        coroutineScope: CoroutineScope,
        input: String,
        wrongChars: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Flow<Pair<Float, Float>> = flow {
        val lines = spreadMatrixOnLines(input)

        for (line in lines)
        {
            if (line.filter { s -> wrongChars.contains(s) } != "") emit(
                getSizeOfLineWithWrongChars(
                    coroutineScope,
                    line,
                    wrongChars,
                    defaultPainter,
                    errorPainter
                )
            )
            else emit(getSizeOfMatrixLine(defaultPainter, line))
        }
    }.flowOn(Dispatchers.Default)

    fun drawMatrixLinesWithWrongChars(
        coroutineScope: CoroutineScope,
        input: String,
        wrongChars: String,
        defaultPainter: Paint,
        errorPainter: Paint
    ): Flow<Bitmap> = flow {
        val lines = spreadMatrixOnLines(input)

        for (line in lines)
        {
            if (line.filter { s -> wrongChars.contains(s) } != "") emit(
                drawLineWithWrongChars(
                    coroutineScope,
                    line,
                    wrongChars,
                    defaultPainter,
                    errorPainter
                )
            )
            else emit(drawMatrixLine(line, defaultPainter))
        }

    }.flowOn(Dispatchers.Default)
}