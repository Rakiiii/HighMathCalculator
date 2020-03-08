package com.dev.smurf.highmathcalculator.Polynomials

import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Utils.*

class ExponensialPolynomial : PolynomialBase
{
    companion object
    {
        val defaultSymbol = "x"
    }

    constructor() { polynomial = arrayListOf()}

    constructor( arr : ArrayList<Pair<Int, ComplexNumber>>) { polynomial = arr }

    constructor(str : String)
    {
        polynomial = arrayListOf()

        //change small degree numbers to normal numbers
        //remove extra spaces
        //replace pluses with spaces
        var pol = str.degreesToNormalForm().filter { s -> ( s != ' ' ) }.removePluses()

        //for amount cofs in polynomial
        for(i in 0 until pol.amountOfCofsInPolinom())
        {
            //get value of cof in polynomial
            var value = pol.substringBefore(' ').substringBefore('^').substringBeforeSymbol()

            //get degree of cof in polynomial
            var exp = pol.substringBefore(' ').substringAfterSymbol()

            //if something wrong
            when
            {
                //if degree of cof is 0
                value == "" && exp == "" ->
                {
                    value = pol.substringBefore(' ')

                    exp = "0"
                }
                //if cof before degree equals 1
                value == "" ->
                {
                    value = "1"
                }
                //if degree is in reconaizable
                exp == "" ->
                {
                    exp = "0"
                }
            }

            //add cof to polynomial
            polynomial.plusToCof(exp.toInt() , value.toComplex())

            pol = pol.substringAfter(' ')
        }

        polynomial.sortBy { s -> s.first }
    }

    //contains polynomial in form degree : cof where degree is int and cof is complex number
    private lateinit var polynomial : ArrayList<Pair<Int,ComplexNumber>>

    //returns polynomials degree
    override fun degree() = polynomial.size

    //returns copy of polynomial
    override fun Copy(): ExponensialPolynomial
    {
        val copy = ExponensialPolynomial()

        for(i in polynomial)
        {
            copy.polynomial.plus(Pair(i.first,i.second.Copy()))
        }

        return copy
    }


    override fun plus(obj: Any): PolynomialBase
    {
        //make copy of left operand
        val result = this.Copy()

        when(obj)
        {
            //if right operand is complex number
            is ComplexNumber ->
            {
                //then we need to plus right operand to 0 degree cof
                result.polynomial.plusToCof(0 , obj)
            }

            //if right operand is exponesial polynomial
            is ExponensialPolynomial ->
            {
                //plus all degrees of right operand to left operand
                for( i in obj.polynomial)
                {
                    result.polynomial.plusToCof(i.first , i.second)
                }
            }

            //in any other case the type of right operand is wrong
            else ->
            {
                throw Exception("Unknown type for polynomial plus")
            }
        }

        return result
    }

    override fun minus(obj: Any): PolynomialBase
    {
        //make copy of left operand
        val result = this.Copy()

        when(obj)
        {
            //if right operand is complex number
            is ComplexNumber ->
            {
                //then we need to minus right operand to 0 degree cof
                result.polynomial.minusToCof(0 , obj)
            }
            //if right operand is exponesial polynomial
            is ExponensialPolynomial ->
            {
                //minus all degrees of right operand to left operand
                for( i in obj.polynomial)
                {
                    result.polynomial.minusToCof(i.first , i.second)
                }
            }

            //in any other case the type of right operand is wrong
            else ->
            {
                throw Exception("Unknown type for polynomial minus")
            }
        }

        return result
    }

    override fun times(obj: Any): PolynomialBase
    {
        //make new polynomial
        val new = ExponensialPolynomial()

        when(obj)
        {
            //if right operand is complex number
            is ComplexNumber->
            {
                //multiple all cofs by right operand
                for(i in polynomial)
                {
                    new.polynomial.plusToCof(i.first , i.second / obj)
                }
            }

            //if right operand is exponensial polynomial
            is ExponensialPolynomial ->
            {
                //multiple each cofs by each cofs and sum them
                for( rightMultiplier in obj.polynomial)
                {
                    for( leftMultiplier in polynomial)
                    {
                        new.polynomial.plusToCof(rightMultiplier.first + leftMultiplier.first , rightMultiplier.second * leftMultiplier.second)
                    }
                }
            }

            else ->
            {
                throw Exception("Unknown type for polynomial multiplication")
            }
        }

        return new
    }

    override fun div(obj: Any): Pair<PolynomialBase, PolynomialBase>
    {
        val result = ExponensialPolynomial()
        val remainder = ExponensialPolynomial()

        when(obj)
        {
            //if right operand is number
            is ComplexNumber ->
            {
                //divide all cofs with this number
                for(i in polynomial)
                {
                    result.polynomial.plusToCof(i.first , i.second / obj)
                }
            }
            is ExponensialPolynomial ->
            {
                //divison function
                  val subRes = exponensialRecursiveDivison( this.Copy().polynomial , obj.polynomial)

                //set polinoms with resuklt arrays
                result.polynomial = subRes.first
                remainder.polynomial = subRes.second

            }
            else ->
            {
                throw Exception("Unknown type for polynomial division")
            }
        }

        return Pair(result , remainder)
    }

    override fun toString(): String
    {
        var string = ""

        for(i in polynomial)
        {
            string += i.second.toString()+ defaultSymbol+"^"+i.first.toString().toDegree()
            if(i != polynomial.last()) string += "+"
        }

        return string
    }

    //returns array of cofs and symbols to render polynomial on canvas
    override fun renderFormat(): ArrayList<Pair<String, ComplexNumber>>
    {
        val renderFormat : ArrayList<Pair<String,ComplexNumber>> = arrayListOf()

        for(i in polynomial)
        {
            renderFormat.add( Pair( defaultSymbol + "^" + i.first.toString().toDegree() , i.second))
        }

        return renderFormat
    }

}

