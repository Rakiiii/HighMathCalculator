package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class WrongSymbolAtExponetialPolynomialInputException(input: String, unrecognizablePart: String):
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "You can use only one variable symbol in exponential polynomial:|$unrecognizablePart| in string:$input"
}
