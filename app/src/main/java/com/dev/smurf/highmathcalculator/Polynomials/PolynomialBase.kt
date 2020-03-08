package com.dev.smurf.highmathcalculator.Polynomials

import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber

abstract class PolynomialBase : PolynomialCoppableInterface
{
    abstract fun degree() : Int

    abstract operator fun plus(obj : Any) : PolynomialBase

    abstract operator fun minus(obj : Any) : PolynomialBase

    abstract operator fun times(obj : Any) : PolynomialBase

    abstract operator fun div(obj : Any) : Pair<PolynomialBase,PolynomialBase>

    abstract fun renderFormat() : ArrayList<Pair<String,ComplexNumber>>
}