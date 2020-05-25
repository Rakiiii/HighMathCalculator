package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class TooManyDegreeSymbolsInExponentialPolynomialVariableException (input: String, unrecognizablePart: String):
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Too many degree symbols in exponential polynomial: $input"
}