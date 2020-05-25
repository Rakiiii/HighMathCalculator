package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class WrongExponensialSymbolPositionException(input: String, unrecognizablePart: String):
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Only variable can have degree, this input is incorrect:|$unrecognizablePart| in string:$input"
}
