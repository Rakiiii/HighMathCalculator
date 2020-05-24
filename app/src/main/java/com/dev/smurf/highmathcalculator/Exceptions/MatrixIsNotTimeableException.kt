package com.dev.smurf.highmathcalculator.Exceptions

open class MatrixIsNotTimeableException : WrongMatrixSizeException()
{
    override val message: String?
        get() = "Is not timeable matrix's size"
}