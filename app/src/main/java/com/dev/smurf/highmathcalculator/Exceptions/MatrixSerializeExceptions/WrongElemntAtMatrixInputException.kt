package com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions

class WrongElemntAtMatrixInputException(input: String, unrecognizedPart: String) :
    WrongMatrixInputFormatException(input = input, unrecognizedPart = unrecognizedPart)
{
    override val message: String?
        get() = "Wrong element: $unrecognizedPart at matrix: $input"
}