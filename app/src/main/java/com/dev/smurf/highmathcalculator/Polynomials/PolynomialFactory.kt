package com.dev.smurf.highmathcalculator.Polynomials

class PolynomialFactory
{
    fun createPolynomial(obj: String): PolynomialBase
    {
        if(obj == "" || obj == " " )return PolynomialBase.EmptyPolynomial
        return if (obj.contains('^')) ExponentialPolynomial.createExponentialPolynomial(obj)
        else LinearDiofantPolynomial.createLinearDiofantPolynomial(
            obj
        )
    }
}