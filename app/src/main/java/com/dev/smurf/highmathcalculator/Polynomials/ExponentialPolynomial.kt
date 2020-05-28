package com.dev.smurf.highmathcalculator.Polynomials

import android.util.Log
import com.dev.smurf.highmathcalculator.Exceptions.PolynomialSerializeExceptions.*
import com.dev.smurf.highmathcalculator.Exceptions.WrongTypeForOperationException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.StringsExtension.*
import com.dev.smurf.highmathcalculator.Utils.*

class ExponentialPolynomial private constructor(
    //contains polynomial in form degree : cof where degree is int and cof is complex number
    private var polynomial: ArrayList<Pair<Int, ComplexNumber>>,
    private val variableSymbol: Char = defaultSymbol
) : PolynomialBase()
{
    companion object
    {
        const val defaultSymbol = 'x'

        @Deprecated("use createExponentialPolynomial instead for input format control")
        fun createExponentialPolynomials(str: String): ExponentialPolynomial
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


        fun createExponentialPolynomial(str: String): ExponentialPolynomial
        {
            isExponentialPolynomial(str)

            val parsed = mutableMapOf<Int, ComplexNumber>()

            var polynomialString =
                str.filterNot { s -> (s == ' ') || (s == '\n') }.degreesToNormalForm().toLowerCase()
            var pos = polynomialString.getFirstCofSeparatorPosition()

            var symbol = defaultSymbol

            while (pos != -1)
            {
                val part = polynomialString.substring(0, pos)
                val cofParsed = parsePart(part)
                if (cofParsed.third != defaultSymbol) symbol = cofParsed.third

                parsed[cofParsed.first] =
                    parsed[cofParsed.first] ?: ComplexNumber() + cofParsed.second

                polynomialString =
                    if (polynomialString[pos] == '-') polynomialString.substring(
                        pos,
                        polynomialString.length
                    )
                    else polynomialString.substring(pos + 1, polynomialString.length)

                pos = polynomialString.getFirstCofSeparatorPosition()
            }
            val cofParsed = parsePart(polynomialString)
            if (cofParsed.third != defaultSymbol) symbol = cofParsed.third

            parsed[cofParsed.first] = parsed[cofParsed.first] ?: ComplexNumber() + cofParsed.second

            val maxDegree = parsed.maxBy { it.key }?.key ?: 0

            for (i in 0..maxDegree)
            {
                if (!parsed.containsKey(i))
                {
                    parsed[i] = ComplexNumber()
                }
            }

            val polynomial = ArrayList<Pair<Int, ComplexNumber>>(0)

            for (part in parsed)
            {
                polynomial.add(Pair(part.key, part.value))
            }

            polynomial.sortBy { it.first }
            polynomial.reverse()

            return ExponentialPolynomial(polynomial = polynomial, variableSymbol = symbol)
        }

        fun parsePart(part: String): Triple<Int, ComplexNumber, Char>
        {
            val cof = part.substringBeforeSymbol('i').toComplexNumber()


            val variable = part.substringAfterSymbolIncluded('i')
            var degree = 0

            if (variable != "")
            {
                degree = variable.substringAfter('^').toInt()
            }

            return Triple(degree, cof, if (variable != "") variable[0] else defaultSymbol)
        }

        fun isExponentialPolynomial(str: String)
        {
            val amountOfLeftBrackets = str.count { s -> s == '(' }
            val amountOfRightBrackets = str.count { s -> s == ')' }
            if (amountOfLeftBrackets != amountOfRightBrackets) throw WrongAmountOfBracketsInPolynomialException(
                str,
                ""
            )

            var polynomial =
                str.filterNot { s -> (s == ' ') || (s == '\n') }.degreesToNormalForm().toLowerCase()
            val wrongSymbolsString = polynomial.filterNot { s -> isGoodSymbol(s) }

            if (wrongSymbolsString != "") throw WrongSymbolInPolynomialInputException(
                str,
                wrongSymbolsString
            )

            var variableChar = ' '

            var pos = polynomial.getFirstCofSeparatorPosition()

            while (pos != -1)
            {
                val partForCheck = polynomial.substring(0, pos)

                val cof = partForCheck.substringBeforeSymbol('i')
                if (cof.isNotComplexNumber()) throw WrongPolynomialCofFormatException(str, cof)

                val variable = partForCheck.substringAfterSymbolIncluded('i')

                variableChar = checkExponentialPolynomialVariable(str, variable, variableChar)

                polynomial =
                    if (polynomial[pos] == '-') polynomial.substring(pos, polynomial.length)
                    else polynomial.substring(pos + 1, polynomial.length)

                pos = polynomial.getFirstCofSeparatorPosition()
            }

            if (polynomial != "")
            {
                val cof = polynomial.substringBeforeSymbol('i')
                if (cof.isNotComplexNumber()) throw WrongPolynomialCofFormatException(str, cof)

                val variable = polynomial.substringAfterSymbolIncluded('i')

                checkExponentialPolynomialVariable(str, variable, variableChar)
            }else throw WrongSymbolInPolynomialInputException(
                str,
                str.last().toString()
            )
        }

        private fun checkExponentialPolynomialVariable(
            str: String,
            variable: String,
            variableChar: Char
        ): Char
        {
            var returnChar: Char = variableChar
            when
            {
                variable == "" ->
                {
                }
                variable.contains('^') ->
                {
                    if (variable.filterNot { s -> (s == '^') || (s in '0'..'9') || (s in 'a'..'z') } != "")
                        throw WrongSymbolAtExponetialPolynomialInputException(
                            str,
                            variable
                        )

                    if (variable.count { s -> s == '^' } != 1) throw TooManyDegreeSymbolsInExponentialPolynomialVariableException(
                        str,
                        ""
                    )


                    val variableSymbol = variable.substringBefore('^')
                    val variableDegree = variable.substringAfter('^')

                    if (variableSymbol == "") throw WrongExponensialSymbolPositionException(
                        str,
                        variable
                    )

                    if (variableSymbol.length != 1) throw WrongExponentialPolynomialVariableFormat(
                        str,
                        variable
                    )

                    if (variableChar == ' ') returnChar = variableSymbol[0]
                    if (variableSymbol[0] != variableChar && variableSymbol[0] != returnChar) throw WrongSymbolAtExponetialPolynomialInputException(
                        str,
                        variable
                    )

                    if (variableDegree.filterNot { s -> s in '0'..'9' } != "" || variableDegree.toIntOrNull() == null) throw WrongSymbolAtExponetialPolynomialInputException(
                        str,
                        variableDegree
                    )
                }
                else ->
                {
                    if (variable.length > 1) throw WrongExponentialPolynomialVariableFormat(
                        str,
                        variable
                    )

                    if (variableChar == ' ') returnChar = variable[0]

                    if (variable[0] != variableChar && variable[0] != returnChar) throw WrongSymbolAtExponetialPolynomialInputException(
                        str,
                        variable
                    )
                }
            }
            return returnChar
        }

        private fun isGoodSymbol(s: Char): Boolean
        {
            return (s in '0'..'9') || (s in 'a'..'z') || (s == '^') || (s == '/') || (s == '+') || (s == '-') || (
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

            //if right operand is exponential polynomial
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
                //division function
                val subRes = exponensialRecursiveDivison(this.Copy().polynomial, obj.polynomial)

                //set polynomial's with result arrays
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

        val filteredPolynomial = polynomial.filter { s -> s.second != ComplexNumber() }

        for (i in filteredPolynomial)
        {
            if (i.second != ComplexNumber())
            {
                string += i.second.toString() + (if (i.first != 0) variableSymbol + "^" + i.first.toString()
                    .toDegree()
                else "")
                if (i != filteredPolynomial.last()) string += "+"
            }
        }

        return string
    }

    //returns array of cofs and symbols to render polynomial on canvas
    override fun renderFormat(): ArrayList<Pair<String, ComplexNumber>>
    {
        val renderFormat: ArrayList<Pair<String, ComplexNumber>> = arrayListOf()

        for (i in polynomial)
        {
            if (i.second != ComplexNumber())
            {
                if (i.first != 0) renderFormat.add(
                    Pair(
                        variableSymbol + "^" + i.first.toString().toDegree(), i.second
                    )
                )
                else renderFormat.add(Pair("", i.second))
            }
        }

        return renderFormat
    }

}

