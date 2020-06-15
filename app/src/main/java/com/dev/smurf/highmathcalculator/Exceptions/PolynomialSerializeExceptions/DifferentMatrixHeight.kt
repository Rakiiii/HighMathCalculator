package com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException

class DifferentMatrixHeight():WrongDataException()
{
    override val message: String?
        get() = "Matrix height is different"
}