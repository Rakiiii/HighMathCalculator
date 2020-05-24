package com.dev.smurf.highmathcalculator.Polynomials

class PolynomialFactory
{
    fun createPolynomial(obj: String): PolynomialBase
    {
        return if (obj.contains('^')) ExponensialPolynomial(obj)
        else DiofantPolynomial.createDiofantPolynomial(
            obj
        )
    }
}