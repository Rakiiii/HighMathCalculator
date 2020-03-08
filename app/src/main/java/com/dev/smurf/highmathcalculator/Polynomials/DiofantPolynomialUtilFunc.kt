package com.dev.smurf.highmathcalculator.Polynomials

import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber

fun diofantPlusToKeyInMap(map :  MutableMap<String , ComplexNumber>, key : String, value : ComplexNumber)
{
    if(map.containsKey(key))
    {
        map[key]?.plus( value)
    }
    else
    {
        map[key] = value
    }
}

fun diofantMinusToKeyInMap(map :  MutableMap<String , ComplexNumber>, key : String, value : ComplexNumber)
{
    if(map.containsKey(key))
    {
        map[key]?.minus( value)
    }
    else
    {
        map[key] = value*(-1)
    }
}