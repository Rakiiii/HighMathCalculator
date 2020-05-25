package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

class WrongExponentialPolynomialVariableFormat(input: String, unrecognizablePart: String):
    WrongPolynomialInputFormatException(input = input, unrecognizablePart = unrecognizablePart)
{
    override val message: String?
        get() = "Wrong format of variable in exponential polynomial input:|$unrecognizablePart| in string:$input"
}
