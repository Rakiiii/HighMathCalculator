package com.dev.smurf.highmathcalculator.Exceptions

open class WrongTypeForOperationException(val type : String) : WrongDataException()
{
    override val message: String?
        get() = "Wrong type for $type operation"
}