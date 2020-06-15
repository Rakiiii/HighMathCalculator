package com.dev.smurf.highmathcalculator.Exceptions

open class WrongDataException() : TimeableException()
{
    override val message: String?
        get() = "Wrong Data Exception"
}
