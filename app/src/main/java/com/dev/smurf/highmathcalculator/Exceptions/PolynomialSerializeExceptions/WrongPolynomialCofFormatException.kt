package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class WrongPolynomialCofFormatException(input: String, unrecognizablePart: String):
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Wrong format of coefficient in polynomial input:$unrecognizablePart in string:$input"
}