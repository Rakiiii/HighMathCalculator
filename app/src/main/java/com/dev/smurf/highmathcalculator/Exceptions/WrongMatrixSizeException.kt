package com.dev.smurf.highmathcalculator.Exceptions

open class WrongMatrixSizeException() : WrongDataException()
{
    override val message: String?
        get() = "Wrong Matrix Size"
}