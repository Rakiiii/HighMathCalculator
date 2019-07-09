package com.dev.smurf.highmathcalculator.Polinoms

import android.util.Log
import com.example.smurf.mtarixcalc.*

class DiofantPolinom : PolinomBase
{
    constructor(obj : String)
    {
        var str = ""
        var res = ""
        if(obj.contains('|'))
        {
            str = obj.substringBefore('|').filter { s -> (s != ' ') }.removePluses()
            res = obj.substringAfter('|')
        }
        else
        {
            str = obj.filter { s -> (s != ' ') }.removePluses()
        }

            Log.d("DEBUG@" , str)
            for(i in 0 until obj.amountOfCofsInPolinom())
            {
                var tmp = str.substringBefore(' ')
                Log.d("DEBUG@" , tmp)
                if(polinom.containsKey(tmp.last().toString()))
                {
                    val tmpValue = polinom[tmp.last().toString()]?.plus(tmp.substringBefore(tmp.last()).toComplex())
                    polinom[tmp.last().toString()] = tmpValue!!
                }
                else
                {
                    polinom[tmp.last().toString()] = tmp.substringBefore(tmp.last()).toComplex()
                }
                str = str.substringAfter(' ')
                Log.d("DEBUG@" , str)
            }
        if(res != "")
        {
            _isSolved = true
            var am = res.countWords()
            for(i in 0 until am)
            {
                var tmp = res.substringBefore(' ')
                var key = tmp.substringBefore(':')
                var value = tmp.substringAfter(':')
                _root[key] = value.toComplex()
            }
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
                    _isSolved = false
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
                _isSolved = false
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

    override fun solve()
    {
        _isSolved = true
    }

    override fun stringWithRoots(): String
    {
        var result = ""
        for(i in _root)
        {
            result += i.key + ":" + i.value.toString() + " "
        }
        return result
    }

    override fun getRoots(): MutableMap<String, complexNumber>
    {
        return _root
    }

    override fun toString(): String
    {
        var res = ""
        for(i in polinom)
        {
            res += i.value.toString()+i.key+"+"
        }
        return res
    }

    override fun isSolved(): Boolean = _isSolved

}