package com.dev.smurf.highmathcalculator.StringsExtension

import android.util.Log
import com.dev.smurf.highmathcalculator.Exceptions.CastException
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber

//add 1 before i in string
fun String.fulfilCofs(): String
{
    var filled = if (first() == 'i') "1$this" else this
    while (filled.indexOf("-i") != -1)
    {
        val pos = filled.indexOf("-i")
        filled = filled.substring(0, pos + 1) + "1" + filled.substring(pos + 1, filled.length)
    }
    while (filled.indexOf("+i") != -1)
    {
        val pos = filled.indexOf("+i")
        filled = filled.substring(0, pos + 1) + "1" + filled.substring(pos + 1, filled.length)
    }

    Log.d("complex@","source:"+this+" filled:"+filled)

    return filled
}

fun String.isComplexNumber(): Boolean
{
    //remove unnecessary symbols
    var trimmed =
        this.filterNot { s -> (s == ' ') || (s == '\n') || (s == '*') }
            .filterNot { s -> (s == '(') || (s == ')') }
            .trim { s -> s == '+' }
    //if contains bad symbols then it's not complex number
    if (trimmed.filterNot { s -> (s in '0'..'9') || (s == '-') || (s == '/') || (s == '.') || (s == 'i') || (s == '+') } != "") return false

    //count amount of good symbols
    val amountOfi = trimmed.count { s -> s == 'i' }
    val amountOfPlus = trimmed.count { s -> s == '+' }
    val amountOfMinus = trimmed.count { s -> s == '-' }

    //if wrong amount then it's not complex number
    if (amountOfi > 1 || amountOfMinus > 2 || amountOfPlus > 1) return false

    //check all possible combinations
    when
    {
        amountOfi == 0 && amountOfPlus == 0 && amountOfMinus < 2 ->
        {
            return (trimmed.isFraction() && if (trimmed.contains('-')) trimmed.first() == '-' else true)
        }

        amountOfi == 0 && amountOfPlus == 0 && amountOfMinus == 2 -> return false
        amountOfi == 0 && amountOfPlus == 1 -> return false
        amountOfi == 1 && amountOfPlus == 0 && amountOfMinus == 0 ->
        {
            return trimmed.last() == 'i' && trimmed.trim { s -> s == 'i' }.isFraction()
        }
        amountOfi == 1 && amountOfPlus == 0 && amountOfMinus == 1 ->
        {
            if (trimmed.first() == '-' && trimmed.last() == 'i' && trimmed.trim { s -> s == 'i' }
                    .isFraction()) return true

            val complex = trimmed.fields("-")
            complex[1] = "-" + complex[1]

            if ((if (complex[0].contains('i')) complex[0].last() != 'i' else false)
                || (if (complex[1].contains('i')) complex[1].last() != 'i' else false)
            ) return false

            return (complex[0].filterNot { s -> s == 'i' }
                .isFraction() && complex[1].filterNot { s -> s == 'i' }.isFraction())

        }
        amountOfi == 1 && amountOfPlus == 0 && amountOfMinus == 2 ->
        {
            val minusPosition = trimmed.indexOfLast { s -> s == '-' }
            if (trimmed[minusPosition - 1] == '-' || trimmed[minusPosition + 1] == '-') return false

            trimmed = trimmed.replaceRange(minusPosition..minusPosition, " ")
            val complex = trimmed.fields(" ")
            complex[1] = "-" + complex[1]

            if ((if (complex[0].contains('i')) complex[0].last() != 'i' else false) ||
                (if (complex[0].contains('i')) complex[0].last() != 'i' else false)
            ) return false

            if ((if (complex[0].contains('-')) complex[0].first() != '-' else false)
                || (if (complex[1].contains('-')) complex[1].first() != '-' else false)
            ) return false

            return (complex[0].filterNot { s -> s == 'i' }
                .isFraction() && complex[1].filterNot { s -> s == 'i' }.isFraction())
        }

        amountOfi == 1 && amountOfPlus == 1 && amountOfMinus < 2 ->
        {
            val complex = trimmed.fields("+")
            if (complex[1] == "" || complex[0] == "") return false

            if ((if (complex[0].contains('i')) complex[0].last() != 'i' else false) ||
                (if (complex[0].contains('i')) complex[0].last() != 'i' else false)
            ) return false

            if (amountOfMinus == 1)
            {
                if ((if (complex[0].contains('-')) complex[0].first() != '-' else false)
                    || (if (complex[1].contains('-')) complex[1].first() != '-' else false)
                ) return false
            }
            return (complex[0].filterNot { s -> s == 'i' }
                .isFraction() && complex[1].filterNot { s -> s == 'i' }.isFraction())
        }

        amountOfi == 1 && amountOfPlus == 1 && amountOfMinus == 2 -> return false
    }

    return true
}

fun String.isNotComplexNumber() = !isComplexNumber()

fun String.toComplexNumber(): ComplexNumber
{
    val filled = fulfilCofs()
    if (filled.isNotComplexNumber())
    {
        Log.d("complex@", filled)
        return ComplexNumber()
    }
    //remove unnessasery symbols
    var trimmed =
        filled.filterNot { s -> (s == ' ') || (s == '\n') || (s == '*') }
            .filterNot { s -> (s == '(') || (s == ')') }
            .trim { s -> s == '+' }

    //count amount of good symbols
    val amountOfi = trimmed.count { s -> s == 'i' }
    val amountOfPlus = trimmed.count { s -> s == '+' }
    val amountOfMinus = trimmed.count { s -> s == '-' }


    //check all possible combinations
    when
    {
        amountOfi == 0 -> return ComplexNumber(_re = trimmed.toFraction())
        amountOfi == 1 && amountOfMinus == 0 && amountOfPlus == 0 ->
            return ComplexNumber(_im = trimmed.filterNot { s -> s == 'i' }.toFraction())
        amountOfi == 1 && amountOfMinus < 2 && amountOfPlus == 1 ->
        {
            val complexNumber = trimmed.fields("+")
            val im =
                if (complexNumber[0].contains('i')) complexNumber[0].filterNot { s -> s == 'i' }
                    .toFraction()
                else complexNumber[1].filterNot { s -> s == 'i' }.toFraction()

            val re =
                if (!complexNumber[0].contains('i')) complexNumber[0].filterNot { s -> s == 'i' }
                    .toFraction()
                else complexNumber[1].filterNot { s -> s == 'i' }.toFraction()

            return ComplexNumber(_re = re, _im = im)
        }
        amountOfi == 1 && amountOfMinus == 1 && amountOfPlus == 0 ->
        {
            val complexNumber = trimmed.fields("-")
            complexNumber[1] = "-" + complexNumber[1]
            //Log.d("complex@","original:"+this+" filled:"+filled+" trimmed:"+trimmed)
            val im =
                if (complexNumber[0].contains('i')) complexNumber[0].filterNot { s -> s == 'i' }
                    .toFraction()
                else complexNumber[1].filterNot { s -> s == 'i' }.toFraction()

            val re =
                if (!complexNumber[0].contains('i')) complexNumber[0].filterNot { s -> s == 'i' }
                    .toFraction()
                else complexNumber[1].filterNot { s -> s == 'i' }.toFraction()
            //Log.d("complex@","original:"+this+" filled:"+filled+" trimmed:"+trimmed+" re:"+re.toString()+" im:"+im.toString())
            return ComplexNumber(_re = re, _im = im)
        }

        amountOfi == 1 && amountOfMinus == 2 && amountOfPlus == 0 ->
        {
            val minusPosition = trimmed.indexOfLast { s -> s == '-' }
            trimmed = trimmed.replaceRange(minusPosition..minusPosition, " ")
            val complexNumber = trimmed.fields(" ")
            complexNumber[1] = "-" + complexNumber[1]

            val im =
                if (complexNumber[0].contains('i')) complexNumber[0].filterNot { s -> s == 'i' }
                    .toFraction()
                else complexNumber[1].filterNot { s -> s == 'i' }.toFraction()

            val re =
                if (!complexNumber[0].contains('i')) complexNumber[0].filterNot { s -> s == 'i' }
                    .toFraction()
                else complexNumber[1].filterNot { s -> s == 'i' }.toFraction()
            return ComplexNumber(_re = re, _im = im)
        }
    }
    throw CastException(arrayOf(amountOfi, amountOfPlus, amountOfMinus))
}

fun String.toComplexNumberOrNull(): ComplexNumber?
{
    return if (isNotComplexNumber()) null
    else this.toComplexNumber()
}