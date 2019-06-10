package com.dev.smurf.highmathcalculator.Polinoms

class PolinomFactory
{
    fun createPolinom(obj : String) : PolinomBase
    {
        if(obj.contains('^'))
        {
            throw Exception("NeedToAddExponentialPolinom")
        }
        else return DiofantPolinom(obj)
    }
}