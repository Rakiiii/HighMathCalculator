package com.dev.smurf.highmathcalculator.Exceptions

open class MatrixNotSquareException : WrongMatrixSizeException()
{
    override val message: String?
        get() = "Matrix is not square"
}