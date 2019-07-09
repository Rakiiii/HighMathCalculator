package com.dev.smurf.highmathcalculator.Polinoms

import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber

class Gene(var alleles : MutableMap<String, ComplexNumber>, var fitnesses : Int, likelihood : Float)
{
    override operator fun equals(other: Any?): Boolean
    {
        when (other)
        {
            is Gene ->
            {
                for( i in alleles)
                {
                    if(other.alleles[i.key]!= i.value)return false
                }
                return true
            }
            else -> return false
        }
    }
}