package com.dev.smurf.highmathcalculator.Exceptions.MatrixLineExceptions

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException

class DifferentLineLengthException() : WrongDataException()
{
    override val message: String?
        get() = "Lines of matrix have different length"
}