package com.dev.smurf.highmathcalculator.Exceptions.MatrixSerializeExceptions

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException

open class WrongMatrixInputFormatException(val input : String,val unrecognizedPart : String) : WrongDataException()
{
    override val message: String?
        get() = "Wrong input: $input , can't recognise: $unrecognizedPart"
}