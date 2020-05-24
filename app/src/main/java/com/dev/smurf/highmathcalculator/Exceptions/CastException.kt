package com.dev.smurf.highmathcalculator.Exceptions

open class CastException(val numberSet : Array<Int>) : WrongDataException()
{
    override val message: String?
        get() = "Wrong cast from string "+numberSet[0].toString() + " " + numberSet[1].toString()+" "+numberSet[2].toString()
}