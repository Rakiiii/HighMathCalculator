package com.dev.smurf.highmathcalculator.Exceptions

open class DifferentElementsInInitStringLinesException : WrongDataException()
{
    override val message: String?
        get() = "Different amount of elements in line of matrix"
}