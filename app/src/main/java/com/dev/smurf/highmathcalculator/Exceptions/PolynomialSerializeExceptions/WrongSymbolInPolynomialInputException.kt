package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class WrongSymbolInPolynomialInputException(input: String, unrecognizablePart: String) :
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Wrong symbols in diofant polynomial input:"+unrecognizablePart+" in string:"+input
}