package com.dev.smurf.highmathcalculator.Exceptions

class OverflowExceptions : WrongDataException()
{
    override val message: String?
        get() = "Numbers are too big"
}