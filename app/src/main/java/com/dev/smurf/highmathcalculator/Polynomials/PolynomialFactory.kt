package com.dev.smurf.highmathcalculator.Polynomials

class PolynomialFactory
{
    fun createPolynomial(obj: String): PolynomialBase
    {
        return if (obj.contains('^')) ExponentialPolynomial.createExponentialPolynomial(obj)
        else LinearDiofantPolynomial.createLinearDiofantPolynomial(
            obj
        )
    }
}