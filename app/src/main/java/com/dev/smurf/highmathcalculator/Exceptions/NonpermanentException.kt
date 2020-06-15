package com.dev.smurf.highmathcalculator.Exceptions

class NonpermanentException(val msg: String) : WrongDataException()
{
    override val message: String?
        get() = msg
}