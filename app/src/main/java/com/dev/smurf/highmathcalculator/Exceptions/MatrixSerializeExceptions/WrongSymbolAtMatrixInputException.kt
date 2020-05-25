package com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions

class WrongSymbolAtMatrixInputException(input: String, unrecognizedPart: String) :
    WrongMatrixInputFormatException(input = input, unrecognizedPart = unrecognizedPart)
{
    override val message: String?
        get() = "Wrong symbol: $unrecognizedPart at matrix: $input"
}