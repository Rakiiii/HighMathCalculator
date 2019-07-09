package com.dev.smurf.highmathcalculator.Polinoms

import com.example.smurf.mtarixcalc.complexNumber

abstract class PolinomBase
{
    protected var polinom : MutableMap<String , complexNumber> = mutableMapOf()

    protected var _root : MutableMap<String , complexNumber> = mutableMapOf()

    protected var _isSolved = false

    fun amountOfRoots() : Int
    {
        return _root.size
    }

    fun getSize() : Int
    {
        return polinom.size
    }

    abstract fun solve()

    abstract fun getRoots() : MutableMap<String,complexNumber>

    abstract operator fun plus(obj : Any) : Any

    abstract operator fun minus(obj : Any) : Any

    abstract operator fun times(obj : Any) : Any

    abstract operator fun div(obj : Any) : Any

    abstract fun stringWithRoots() : String

    abstract fun isSolved() : Boolean


}