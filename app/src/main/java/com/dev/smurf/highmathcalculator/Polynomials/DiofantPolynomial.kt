package com.dev.smurf.highmathcalculator.Polynomials

import com.dev.smurf.highmathcalculator.Exceptions.NoOperationForTypesException
import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException
import com.dev.smurf.highmathcalculator.Exceptions.WrongTypeForOperationException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Utils.amountOfCofsInPolinom
import com.dev.smurf.highmathcalculator.Utils.removePluses
import com.dev.smurf.highmathcalculator.Utils.toComplex

class DiofantPolynomial private constructor(private val polynomial : MutableMap<String , ComplexNumber>): PolynomialBase()
{

    private constructor() : this(polynomial = mutableMapOf<String , ComplexNumber>())

    companion object
    {
        fun createDiofantPolynomial(obj : String) : DiofantPolynomial
        {
            val polynomial : MutableMap<String , ComplexNumber> = mutableMapOf()
            var str = obj.substringBefore('|').filter { s -> (s != ' ') }.removePluses()

            for(i in 0 until obj.amountOfCofsInPolinom())
            {
                //get one cof
                val tmp = str.substringBefore(' ')

                //add new cof to polynomial
                //if cof contained
                if(polynomial.containsKey(tmp.last().toString()))
                {
                    //get cof from string and convert it to complex number then plus it allready add cof
                    val tmpValue = polynomial[tmp.last().toString()]?.plus(tmp.substringBefore(tmp.last()).toComplex())

                    //change cof
                    polynomial[tmp.last().toString()] = tmpValue!!
                }
                else
                {
                    //add new cof with new symbol
                    polynomial[tmp.last().toString()] = tmp.substringBefore(tmp.last()).toComplex()
                }
                str = str.substringAfter(' ')
            }

            return DiofantPolynomial(polynomial)
        }

        fun createEmptyPolynomial() = DiofantPolynomial()
    }
    /*
    constructor(obj : String)
    {
        var str = obj.substringBefore('|').filter { s -> (s != ' ') }.removePluses()

            for(i in 0 until obj.amountOfCofsInPolinom())
            {
                //get one cof
                val tmp = str.substringBefore(' ')

                //add new cof to polynomial
                //if cof contained
                if(polynomial.containsKey(tmp.last().toString()))
                {
                    //get cof from string and convert it to complex number then plus it allready add cof
                    val tmpValue = polynomial[tmp.last().toString()]?.plus(tmp.substringBefore(tmp.last()).toComplex())

                    //change cof
                    polynomial[tmp.last().toString()] = tmpValue!!
                }
                else
                {
                    //add new cof with new symbol
                    polynomial[tmp.last().toString()] = tmp.substringBefore(tmp.last()).toComplex()
                }
                str = str.substringAfter(' ')
            }
    }*/

    override fun plus(obj: Any) : PolynomialBase
    {
            val res = this.Copy()
            when(obj)
            {
                is DiofantPolynomial ->
                {
                    for(i in obj.polynomial)
                    {
                        diofantPlusToKeyInMap(res.polynomial,i.key,i.value)
                    }
                    return res
                }
                is ComplexNumber ->
                {
                    diofantPlusToKeyInMap(res.polynomial,"0",obj)
                    return res
                }
                else -> throw WrongTypeForOperationException("plus")
            }
    }

    override fun minus(obj: Any) : PolynomialBase
    {
        val res = this.Copy()
        when(obj)
        {
            is DiofantPolynomial ->
            {
                for(i in obj.polynomial)
                {
                    diofantMinusToKeyInMap(res.polynomial, i.key , i.value)
                }
                return res
            }
            is ComplexNumber ->
            {
                diofantMinusToKeyInMap(res.polynomial, "0", obj)
                return res
            }
            else -> throw WrongTypeForOperationException("minus")
        }
    }

    override fun times(obj: Any) : PolynomialBase
    {
        throw NoOperationForTypesException("time","Diofant Polynomial")
    }

    override fun div(obj: Any) : Pair<PolynomialBase,PolynomialBase>
    {
        throw NoOperationForTypesException("division","Diofant Polynomial")
    }


    override fun Copy(): DiofantPolynomial
    {
        val copy = DiofantPolynomial()

        for(i in this.polynomial)
        {
            copy.polynomial[i.key] = i.value.Copy()
        }
        return copy
    }


    override fun degree(): Int
    {
        return polynomial.size
    }


    override fun toString(): String
    {
        var string = ""

        for( i in polynomial)
        {
            string += i.value.toString() + i.key + "+"
        }
        string = string.substringBeforeLast("+")

        return string
    }

    //returns array of cofs and symbols to render polynomial on canvas
    override fun renderFormat(): ArrayList<Pair<String, ComplexNumber>>
    {
        val renderFormat : ArrayList<Pair<String,ComplexNumber>> = arrayListOf()

        for(i in polynomial)
        {
            renderFormat.add(Pair(i.key,i.value))
        }

        return renderFormat
    }
}