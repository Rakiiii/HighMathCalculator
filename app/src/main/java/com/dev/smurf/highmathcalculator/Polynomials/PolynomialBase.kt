package com.dev.smurf.highmathcalculator.Polynomials

import com.dev.smurf.highmathcalculator.Exceptions.WrongTypeForOperationException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber

abstract class PolynomialBase : PolynomialCoppableInterface
{
    abstract fun degree() : Int

    abstract operator fun plus(obj : Any) : PolynomialBase

    abstract operator fun minus(obj : Any) : PolynomialBase

    abstract operator fun times(obj : Any) : PolynomialBase

    abstract operator fun div(obj : Any) : Pair<PolynomialBase,PolynomialBase>

    abstract fun renderFormat() : ArrayList<Pair<String,ComplexNumber>>

    object EmptyPolynomial : PolynomialBase(){
        override fun degree(): Int
        {
            return 0
        }

        override fun plus(obj: Any): PolynomialBase
        {
            if (obj is PolynomialBase)return obj
            else throw WrongTypeForOperationException("plus")
        }

        override fun minus(obj: Any): PolynomialBase
        {
            if (obj is PolynomialBase)return obj
            else throw WrongTypeForOperationException("minus")
        }

        override fun times(obj: Any): PolynomialBase
        {
            if (obj is PolynomialBase)return obj
            else throw WrongTypeForOperationException("times")
        }

        override fun div(obj: Any): Pair<PolynomialBase, PolynomialBase>
        {
            if (obj is PolynomialBase)return Pair(obj,obj)
            else throw WrongTypeForOperationException("div")
        }

        override fun renderFormat(): ArrayList<Pair<String, ComplexNumber>>
        {
            return ArrayList()
        }

        override fun Copy(): PolynomialBase
        {
            return PolynomialBase.EmptyPolynomial
        }
    }
}