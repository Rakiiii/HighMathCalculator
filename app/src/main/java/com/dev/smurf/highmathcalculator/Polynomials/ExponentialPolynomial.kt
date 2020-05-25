package com.dev.smurf.highmathcalculator.Polynomials

import android.util.Log
import com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions.WrongSymbolInPolynomialInputException
import com.dev.smurf.highmathcalculator.Exceptions.WrongTypeForOperationException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.StringsExtension.*
import com.dev.smurf.highmathcalculator.Utils.*

class ExponentialPolynomial private constructor(
    //contains polynomial in form degree : cof where degree is int and cof is complex number
    private var polynomial: ArrayList<Pair<Int, ComplexNumber>>
) : PolynomialBase()
{
    companion object
    {
        const val defaultSymbol = "x"

        fun createExponentialPolynomial(str: String): ExponentialPolynomial
        {
            val polynomial = arrayListOf<Pair<Int, ComplexNumber>>()

            //change small degree numbers to normal numbers
            //remove extra spaces
            //replace pluses with spaces
            val pol =
                str.degreesToNormalForm().filter { s -> (s != ' ') }.removePluses().fields(" ")

            for (i in 0 until pol.size)
            {
                //get value of cof in polynomial
                var value = pol[i].substringBefore('^').substringBeforeSymbol()

                //get degree of cof in polynomial
                var exp = pol[i].substringAfterSymbol().substringAfter('^')

                //if something wrong
                when
                {
                    //if degree of cof is 0
                    value == "" && exp == "" ->
                    {
                        value = pol[i]

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

                if (!pol[i].contains("^"))
                {
                    exp = "0"
                }
                //add cof to polynomial
                polynomial.plusToCof(exp.toInt(), value.toComplexNumber())

            }
            polynomial.sortBy { s -> s.first }
            polynomial.reverse()

            return ExponentialPolynomial(polynomial)
        }

        fun createEmptyExponentialPolynomial() = ExponentialPolynomial()

        fun isExponentialPolynomial(str: String)
        {
            val trimmed = str.filterNot { s -> (s == ' ') || (s == '\n') }.degreesToNormalForm().toLowerCase()
            val filtered = trimmed.filterNot { s -> isGoodSymbol(s)}

            if (filtered != "")throw WrongSymbolInPolynomialInputException(str,filtered)
        }

        private fun isGoodSymbol(s: Char): Boolean
        {
            return (s in '0'..'9') || (s in 'a'..'z') || (s == '^') || (s == '/') || (s == '+') || (s == '-') ||(
                s == '.'
            ) || (s == '(') || (s == ')')
        }
    }

    private constructor() : this(polynomial = arrayListOf<Pair<Int, ComplexNumber>>())

    //returns polynomials degree
    override fun degree() = polynomial.size

    //returns copy of polynomial
    override fun Copy(): ExponentialPolynomial
    {
        val copy = ExponentialPolynomial()

        for (i in polynomial)
        {
            copy.polynomial.add(Pair(i.first, i.second.Copy()))
        }

        return copy
    }


    override fun plus(obj: Any): PolynomialBase
    {
        //make copy of left operand
        val result = this.Copy()

        when (obj)
        {
            //if right operand is complex number
            is ComplexNumber ->
            {
                //then we need to plus right operand to 0 degree cof
                result.polynomial.plusToCof(0, obj)
            }

            //if right operand is exponesial polynomial
            is ExponentialPolynomial ->
            {
                Log.d("plus@", "start plussing")
                //plus all degrees of right operand to left operand
                for (i in obj.polynomial)
                {
                    result.polynomial.plusToCof(i.first, i.second)
                }
            }

            //in any other case the type of right operand is wrong
            else ->
            {
                throw WrongTypeForOperationException("plus")
            }
        }

        return result
    }

    override fun minus(obj: Any): PolynomialBase
    {
        //make copy of left operand
        val result = this.Copy()

        when (obj)
        {
            //if right operand is complex number
            is ComplexNumber ->
            {
                //then we need to minus right operand to 0 degree cof
                result.polynomial.minusToCof(0, obj)
            }
            //if right operand is exponesial polynomial
            is ExponentialPolynomial ->
            {
                //minus all degrees of right operand to left operand
                for (i in obj.polynomial)
                {
                    result.polynomial.minusToCof(i.first, i.second)
                }
            }

            //in any other case the type of right operand is wrong
            else ->
            {
                throw WrongTypeForOperationException("minus")
            }
        }

        return result
    }

    override fun times(obj: Any): PolynomialBase
    {
        //make new polynomial
        val new = ExponentialPolynomial()

        when (obj)
        {
            //if right operand is complex number
            is ComplexNumber ->
            {
                //multiple all cofs by right operand
                for (i in polynomial)
                {
                    new.polynomial.plusToCof(i.first, i.second / obj)
                }
            }

            //if right operand is exponensial polynomial
            is ExponentialPolynomial ->
            {
                //multiple each cofs by each cofs and sum them
                for (rightMultiplier in obj.polynomial)
                {
                    for (leftMultiplier in polynomial)
                    {
                        new.polynomial.plusToCof(
                            rightMultiplier.first + leftMultiplier.first,
                            rightMultiplier.second * leftMultiplier.second
                        )
                    }
                }
            }

            else ->
            {
                throw WrongTypeForOperationException("times")
            }
        }

        return new
    }

    override fun div(obj: Any): Pair<PolynomialBase, PolynomialBase>
    {
        val result = ExponentialPolynomial()
        val remainder = ExponentialPolynomial()

        when (obj)
        {
            //if right operand is number
            is ComplexNumber ->
            {
                //divide all cofs with this number
                for (i in polynomial)
                {
                    result.polynomial.plusToCof(i.first, i.second / obj)
                }
            }
            is ExponentialPolynomial ->
            {
                //divison function
                val subRes = exponensialRecursiveDivison(this.Copy().polynomial, obj.polynomial)

                //set polinoms with resuklt arrays
                result.polynomial = subRes.first
                remainder.polynomial = subRes.second

            }
            else ->
            {
                throw WrongTypeForOperationException("division")
            }
        }

        return Pair(result, remainder)
    }

    override fun toString(): String
    {
        var string = ""

        for (i in polynomial)
        {
            string += i.second.toString() + defaultSymbol + "^" + i.first.toString().toDegree()
            if (i != polynomial.last()) string += "+"
        }

        return string
    }

    //returns array of cofs and symbols to render polynomial on canvas
    override fun renderFormat(): ArrayList<Pair<String, ComplexNumber>>
    {
        val renderFormat: ArrayList<Pair<String, ComplexNumber>> = arrayListOf()

        for (i in polynomial)
        {
            if (i.first != 0) renderFormat.add(
                Pair(
                    defaultSymbol + "^" + i.first.toString().toDegree(), i.second
                )
            )
            else renderFormat.add(Pair("", i.second))
        }

        return renderFormat
    }

}

