package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class WrongAmountOfBracketsInPolynomialException(input: String, unrecognizablePart: String):
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Different amount of brackets in polynomial input"
}
