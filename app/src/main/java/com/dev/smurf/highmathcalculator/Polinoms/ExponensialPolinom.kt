package com.dev.smurf.highmathcalculator.Polinoms

import com.example.smurf.mtarixcalc.*

class ExponensialPolinom : PolinomBase
{
    constructor(pol : String )
    {
        var  param = ""
        var res = ""
        if(pol.contains('|'))
        {
            param = pol.substringBefore('|')
            res = pol.substringAfter('|')
            _isSolved = true
        }
        else
        {
            param = pol
        }
        var obj = param.degreesToNormalForm()
        var str = obj.filter { s -> ( s != ' ' ) }.removePluses()
        for( i in 0 until obj.amountOfCofsInPolinom())
        {
            var value = str.substringBefore(' ').substringBefore('^').substringBeforSymbol()
            var exp = str.substringBefore(' ').substringAfterSymbolIncluded()
            if(polinom.containsKey(exp))
            {
                val tmpValue = polinom.get(exp)?.plus(value.toComplex())
                polinom[exp] = tmpValue!!
            }
            else
            {
                polinom[exp] = value.toComplex()
            }
            str = str.substringAfter(' ')
        }
        if(res != "")
        {
            var tmp = res.degreesToNormalForm()
            for(i in 0 until tmp.amountOfCofsInPolinom())
            {
                _root[tmp.substringBefore(' ').substringAfterSymbolIncluded()] = tmp.
                    substringBefore(' ').
                    substringBefore('^').
                    substringBeforSymbol().
                    toComplex()
                tmp = tmp.substringAfter(' ')
            }
        }
    }

    override fun plus(obj: Any): Any
    {
        when(obj)
        {
            is ExponensialPolinom ->
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
            is ExponensialPolinom ->
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

    override fun div(obj: Any): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun times(obj: Any): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRoots(): MutableMap<String, complexNumber> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun solve() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stringWithRoots(): String
    {
        var result = ""
        for(i in _root)
        {
            result +=i.key.substringBefore('^') + i.key.substringAfter('^').toDegree() + ":" + i.value.toString() + " "
        }
        return result
    }

    override fun toString(): String
    {
        var res = ""
        for(i in polinom)
        {
            res += i.value.toString()+i.key.substringBefore('^')+i.key.substringAfter('^').toDegree()+"+"
        }
        return res
    }

    override fun isSolved(): Boolean = _isSolved
}