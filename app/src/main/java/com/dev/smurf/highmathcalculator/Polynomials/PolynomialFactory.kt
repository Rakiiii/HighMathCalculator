package com.dev.smurf.highmathcalculator.Polynomials

class PolynomialFactory
{
    fun createPolynomial(obj: String): PolynomialBase
    {
        if(obj == "" || obj.substringBefore('@') == PolynomialBase.EmptyPolynomial.supString().substringBefore('@'))return PolynomialBase.EmptyPolynomial
        return if (obj.contains('^')) ExponentialPolynomial.createExponentialPolynomial(obj)
        else LinearDiofantPolynomial.createLinearDiofantPolynomial(
            obj
        )
    }
}