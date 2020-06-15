package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException

class OverflowException():WrongDataException()
{
    override val message: String?
        get() = "Numbers are too big"
}