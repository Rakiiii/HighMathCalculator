package com.dev.smurf.highmathcalculator.Polynomials

import com.dev.smurf.highmathcalculator.Exceptions.*
import com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions.WrongDiofantPolynomialCofFormatException
import com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions.WrongDiofantPolynomialVariableLengthException
import com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions.WrongSymbolInDiofantPolynomialInputException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.StringsExtension.*

class DiofantPolynomial private constructor(private val polynomial: MutableMap<String, ComplexNumber>) :
    PolynomialBase()
{

    private constructor() : this(polynomial = mutableMapOf<String, ComplexNumber>())

    companion object
    {

        fun createDiofantPolynomial(str : String) : DiofantPolynomial
        {
            isDiofantPolynomial(str)
            val polynomial: MutableMap<String, ComplexNumber> = mutableMapOf()

            var polynomialString = str.filterNot { s -> (s == ' ') || (s == '\n') }.toLowerCase().fulfilCofs()

            var pos = polynomialString.getFirstCofSeparatorPosition()
            while (pos != -1)
            {
                val part = polynomialString.substring(0,pos)
                val variable = part.substringAfterSymbolIncluded('i')
                val cof = part.substringBeforeSymbol('i')

                //if variable allready existed then we plus it cof with this one, if not existe then add this
                //plus to 0 complex number as same as set new
                polynomial[variable] = polynomial[variable]?: ComplexNumber() + cof.toComplexNumber()

                polynomialString =
                    if (polynomialString[pos] == '-') polynomialString.substring(pos,polynomialString.length)
                    else polynomialString.substring(pos + 1,polynomialString.length)

                pos = polynomialString.getFirstCofSeparatorPosition()
            }
            val variable = polynomialString.substringAfterSymbolIncluded('i')
            val cof = polynomialString.substringBeforeSymbol('i')
            polynomial[variable] = polynomial[variable]?: ComplexNumber() + cof.toComplexNumber()

            return DiofantPolynomial(polynomial)
        }

        fun createEmptyPolynomial() = DiofantPolynomial()


        fun isDiofantPolynomial(str: String)
        {

            var polynomial = str.filterNot { s -> (s == ' ') || (s == '\n') }.toLowerCase().fulfilCofs()
            val wrongSymbolString = polynomial.filterNot { s ->
                (s in '0'..'9') || (s in 'a'..'z') || (s == '/') || (s == '.') || (s == '(') || (s == ')') || (s == '+')
                        || (s == '-' || (s == 'i'))
            }
            if (wrongSymbolString != "") throw WrongSymbolInDiofantPolynomialInputException(str,wrongSymbolString)

            var pos = polynomial.getFirstCofSeparatorPosition()
            while (pos != -1)
            {

                val cofForCheck = polynomial.substring(0 , pos)

                //Log.d("pol@","cofForCheck:"+cofForCheck+" pos:"+pos.toString()+" indefOf:"+polynomial.indexOf('+').toString())

                val variable = cofForCheck.substringAfterSymbolIncluded('i')
                if (variable.length > 1) throw WrongDiofantPolynomialVariableLengthException(polynomial,variable)
                val cof = cofForCheck.substringBeforeSymbol('i')
                if (cof.isNotComplexNumber()) throw WrongDiofantPolynomialCofFormatException(polynomial,cof)

                polynomial =
                    if (polynomial[pos] == '-') polynomial.substring(pos,polynomial.length)
                    else polynomial.substring(pos + 1,polynomial.length)

                pos = polynomial.getFirstCofSeparatorPosition()
            }
            if (polynomial != "")
            {
                val variable = polynomial.substringAfterSymbolIncluded('i')
                if (variable.length > 1) throw WrongDiofantPolynomialVariableLengthException(str,variable)
                val cof = polynomial.substringBeforeSymbol('i')
                if (cof.isNotComplexNumber()) throw WrongDiofantPolynomialCofFormatException(str,cof)
            }
        }
    }

    override fun plus(obj: Any): PolynomialBase
    {
        val res = this.Copy()
        when (obj)
        {
            is DiofantPolynomial ->
            {
                for (i in obj.polynomial)
                {
                    diofantPlusToKeyInMap(res.polynomial, i.key, i.value)
                }
                return res
            }
            is ComplexNumber ->
            {
                diofantPlusToKeyInMap(res.polynomial, "0", obj)
                return res
            }
            else -> throw WrongTypeForOperationException("plus")
        }
    }

    override fun minus(obj: Any): PolynomialBase
    {
        val res = this.Copy()
        when (obj)
        {
            is DiofantPolynomial ->
            {
                for (i in obj.polynomial)
                {
                    diofantMinusToKeyInMap(res.polynomial, i.key, i.value)
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

    override fun times(obj: Any): PolynomialBase
    {
        throw NoOperationForTypesException("time", "Diofant Polynomial")
    }

    override fun div(obj: Any): Pair<PolynomialBase, PolynomialBase>
    {
        throw NoOperationForTypesException("division", "Diofant Polynomial")
    }


    override fun Copy(): DiofantPolynomial
    {
        val copy = DiofantPolynomial()

        for (i in this.polynomial)
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

        for (i in polynomial)
        {
            string += i.value.toString() + i.key + "+"
        }
        string = string.substringBeforeLast("+")

        return string
    }

    //returns array of cofs and symbols to render polynomial on canvas
    override fun renderFormat(): ArrayList<Pair<String, ComplexNumber>>
    {
        val renderFormat: ArrayList<Pair<String, ComplexNumber>> = arrayListOf()

        for (i in polynomial)
        {
            renderFormat.add(Pair(i.key, i.value))
        }

        return renderFormat
    }
}