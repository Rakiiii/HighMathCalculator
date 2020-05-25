package com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException

open class DifferentAmountOfElementsInMatrixLineException(input: String, unrecognizedPart: String) :
    WrongMatrixInputFormatException(input = input, unrecognizedPart = unrecognizedPart)
{
    override val message: String?
        get() = "Different line length at line: $unrecognizedPart"
}