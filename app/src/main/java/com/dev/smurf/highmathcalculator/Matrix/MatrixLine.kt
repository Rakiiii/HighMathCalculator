package com.dev.smurf.highmathcalculator.Matrix

import com.dev.smurf.highmathcalculator.Exceptions.MatrixLineExceptions.DifferentLineLengthException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import java.text.FieldPosition

class MatrixLine private constructor(val length: Int, private val line: Array<ComplexNumber>)
{
    companion object{
        fun createArrayOfMatrixLines(matrix: Matrix) : MutableList<MatrixLine>
        {
            val arrayOfMatrixLine = arrayListOf<MatrixLine>()
            for (i in matrix.rowIndices())
            {
                arrayOfMatrixLine.add(i,MatrixLine(matrix.width,matrix[i]))
            }
            return arrayOfMatrixLine
        }
    }

    operator fun get(i: Int) = if (i < length) line[i] else ComplexNumber()

    operator fun plus(rightLine: MatrixLine): MatrixLine
    {
        if (this.length != rightLine.length) throw DifferentLineLengthException()

        val newLine = Array(length) { pos ->
            this[pos] + rightLine[pos]
        }

        return MatrixLine(length, newLine)
    }

    operator fun minus(rightLine: MatrixLine): MatrixLine
    {
        if (this.length != rightLine.length) throw DifferentLineLengthException()

        val newLine = Array(length) { pos ->
            this[pos] - rightLine[pos]
        }

        return MatrixLine(length, newLine)
    }

    private operator fun times(number: Any): MatrixLine
    {
        val newLine = Array(length) { pos ->
            this[pos] * number
        }

        return MatrixLine(length, newLine)
    }

    private operator fun div(number: Any): MatrixLine
    {
        val newLine = Array(length) { pos ->
            this[pos] / number
        }

        return MatrixLine(length, newLine)
    }

    operator fun times(number: Int) = this * (number as Any)

    operator fun times(number: Fraction) = this * (number as Any)


    operator fun times(number: ComplexNumber) = this * (number as Any)

    operator fun div(number: Int) = this / (number as Any)
    operator fun div(number: Fraction) = this / (number as Any)
    operator fun div(number: ComplexNumber) = this / (number as Any)

    fun last() = line.last()
}