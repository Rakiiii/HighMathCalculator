package com.dev.smurf.highmathcalculator.Polinoms

import com.example.smurf.mtarixcalc.complexNumber
import com.example.smurf.mtarixcalc.countWords
import com.example.smurf.mtarixcalc.toComplex

class DiofantPolinom : PolinomBase
{
    constructor(obj : String)
    {
        var str = obj.trim().filter { s -> (s != '+') }
        for(i in 0 until str.countWords())
        {
            var tmp = str.substringBefore(' ')
            polinom[tmp.last().toString()] = tmp.substringBefore(tmp.last()).toComplex()
        }
    }

    override fun plus(obj: Any) : Any
    {
            when(obj)
            {
                is DiofantPolinom ->
                {
                    for(i in obj.polinom)
                    {
                        if(polinom.containsKey(i.key))
                        {
                            polinom[i.key]?.plus( i.value)
                        }
                        else
                        {
                            polinom[i.key] = i.value
                        }
                    }
                    _root.withDefault { s -> complexNumber() }
                    return this
                }
                else -> throw Exception("UnknownTypeForPolinomPlus")
            }
    }

    override fun minus(obj: Any): Any
    {
        when(obj)
        {
            is DiofantPolinom ->
            {
                for(i in obj.polinom)
                {
                    if(polinom.containsKey(i.key))
                    {
                        polinom[i.key]?.plus( i.value)
                    }
                    else
                    {
                        polinom[i.key] = i.value
                    }
                }
                _root.withDefault { s -> complexNumber() }
                return this
            }
            else -> throw Exception("UnknownTypeForPolinomMinus")
        }
    }

    override fun times(obj: Any): Any
    {
        throw Exception("NoOperationTimesForDiofantPolinom")
    }

    override fun div(obj: Any): Any
    {
        throw Exception("NoOperationDivisionForDiofantPolinom")
    }

    override fun root(): Map<String, complexNumber>
    {
        return _root
    }

}