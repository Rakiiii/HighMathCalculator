package com.dev.smurf.highmathcalculator.Polinoms

import com.example.smurf.mtarixcalc.complexNumber

abstract class PolinomBase
{
    protected var polinom : MutableMap<String , complexNumber> = mutableMapOf()

    protected var _root : Map<String , complexNumber> = mutableMapOf()

    fun getSize() : Int
    {
        return polinom.size
    }

    abstract fun root() : Map<String ,  complexNumber>

    abstract operator fun plus(obj : Any) : Any

    abstract operator fun minus(obj : Any) : Any

    abstract operator fun times(obj : Any) : Any

    abstract operator fun div(obj : Any) : Any


}