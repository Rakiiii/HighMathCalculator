package com.dev.smurf.highmathcalculator.mvp.models

import android.graphics.*
import android.util.Log
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

class InputFormatExceptionsRenderModel
{

    var screenWidth = 0.0f
    var screenHeight = 0.0f
    fun getHeightMargin() = screenHeight / 20
    fun getWidthMargin() = screenWidth / 20

    val bitmapConfig = Bitmap.Config.ARGB_8888

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
                (size.first + leftFloat).toInt(),
                (size.second + topFloat).toInt(),
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

            Pair(overallWidth, maxHeight+defaultPainter.getVerticalSpacing())
        }
    }

    fun getSizeOfMatrixLine(defaultPainter: Paint, input: String): Pair<Float, Float>
    {
        val rect = Rect()
        defaultPainter.getTextBounds(input, 0, input.length, rect)

        val width = defaultPainter.measureText(input)

        return Pair(width, rect.height().toFloat()+defaultPainter.getVerticalSpacing())
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
}