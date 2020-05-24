package com.dev.smurf.highmathcalculator.Exceptions

open class WrongDataException() : Exception()
{
    override val message: String?
        get() = "Wrong Data Exception"
}
