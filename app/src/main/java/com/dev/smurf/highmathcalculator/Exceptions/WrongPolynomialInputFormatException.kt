package com.dev.smurf.highmathcalculator.Exceptions

open class WrongPolynomialFormatException(val input: String, val unrecognizablePart: String) :
    WrongDataException()
{
    override val message: String?
        get() = "Wrong format for polynomial input"
}

class WrongDiofantPolynomialVariableLength(input: String, unrecognizablePart: String) :
    WrongPolynomialFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Wrong length of Variable in diofant polynomial input"
}
