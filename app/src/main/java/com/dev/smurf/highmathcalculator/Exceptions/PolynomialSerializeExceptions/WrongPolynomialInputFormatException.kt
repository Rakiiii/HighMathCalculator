package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException

open class WrongPolynomialInputFormatException(val input: String, val unrecognizablePart: String) :
    WrongDataException()
{
    override val message: String?
        get() = "Wrong format for polynomial input"
}


