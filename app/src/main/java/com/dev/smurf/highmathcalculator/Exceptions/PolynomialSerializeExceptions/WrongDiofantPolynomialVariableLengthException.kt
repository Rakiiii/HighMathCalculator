package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class WrongDiofantPolynomialVariableLengthException(input: String, unrecognizablePart: String) :
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Wrong length of Variable in diofant polynomial input:"+unrecognizablePart+" in string:"+input
}