package com.dev.smurf.highmathcalculator.Polinoms

class PolinomFactory
{
    fun createPolinom(obj : String) : PolinomBase
    {
        if(obj.contains('^'))
        {
            return ExponensialPolinom(obj)
        }
        else return DiofantPolinom(obj)
    }
}