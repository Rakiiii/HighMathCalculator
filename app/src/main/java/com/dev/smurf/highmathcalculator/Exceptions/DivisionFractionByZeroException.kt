package com.dev.smurf.highmathcalculator.Exceptions

open class DivisionFractionByZeroException : WrongDataException()
{
    override val message: String?
        get() = "Can't divide fraction by zero"
}