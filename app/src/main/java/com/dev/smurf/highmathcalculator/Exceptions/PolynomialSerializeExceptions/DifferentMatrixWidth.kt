package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException

class DifferentMatrixWidth():WrongDataException()
{
    override val message: String?
        get() = "Different matrix width"
}