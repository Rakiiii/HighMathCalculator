package com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions

class WrongAmountOfBracketsInMatrixException(input: String, unrecognizedPart: String) :
    WrongMatrixInputFormatException(input = input, unrecognizedPart = unrecognizedPart)
{
    override val message: String?
        get() = "Different amount of left and right brackets"
}
