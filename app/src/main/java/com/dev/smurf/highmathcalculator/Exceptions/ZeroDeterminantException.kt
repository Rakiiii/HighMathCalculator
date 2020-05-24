package com.dev.smurf.highmathcalculator.Exceptions

open class ZeroDeterminantException : WrongDataException()
{
    override val message: String?
        get() = "Determinant is zero"
}