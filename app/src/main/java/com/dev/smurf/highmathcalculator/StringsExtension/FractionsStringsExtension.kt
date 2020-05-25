package com.dev.smurf.highmathcalculator.StringsExtension

import com.dev.smurf.highmathcalculator.Numbers.Fraction

fun String.isFraction(): Boolean
{
    if (isDecimalFraction()) return true

    //remove some symbols that are not important, plus start and end breakets
    val trimmed =
        this.filterNot { s -> (s == ' ') || (s == '\n') ||  (s == '(') || (s == ')') }

    //if after all string contains any other symbols then digits,minus and devision then it's not fraction
    if (trimmed.filterNot { s -> (s in '0'..'9') || (s == '-') || (s == '/') } != "")
        return false

    if (trimmed.count{ s -> s =='/'} > 1 || trimmed.count{ s -> s =='-'} > 1)return false

    //if can fields then fields
    if (trimmed.contains('/'))
    {
        val fraction = trimmed.fields("/")
        //check is minus sign correct
        if (trimmed.contains('-'))
        {
            if (fraction[1].contains('-')) return false
            if (fraction[0].first() != '-') return false
            //check is number at fraction are correct
            if (fraction[0].toIntOrNull() == null || fraction[1].toIntOrNull() == null) return false
        }
    }
    else
    {
        //check is minus correct
        if (trimmed.contains('-') && trimmed.first() != '-') return false
        //check is number correct
        if (trimmed.toIntOrNull() == null) return false
    }
    return true
}

fun String.isNotFraction() = !isFraction()

fun String.isDecimalFraction(): Boolean
{
    //remove some symbols that are not important, plus start and end breakets
    val trimmed =
        this.filterNot { s -> (s == ' ') || (s == '\n') || (s == '(') || (s == ')') }

    //if after all string contains any other symbols then digits,minus and devision then it's not fraction
    if (trimmed.filterNot { s -> (s in '0'..'9') || (s == '-') || (s == '.') } != "")
        return false

    if (trimmed.count{ s -> s =='.'} > 1 || trimmed.count{ s -> s =='-'} > 1)return false
    //if we have no symbols after or before dots [ "123." or ".1234" ] it's not looks like decimal fraction
    if(trimmed.substringAfter('.') == "" || trimmed.substringBefore('.') == "")return false
    return trimmed.filterNot { s -> s == '.' }.toIntOrNull() != null
}

fun String.isNotDecimalFraction() = !isDecimalFraction()


fun String.toFraction(): Fraction
{
    if (isNotFraction()) return Fraction()

    //remove some symbols that are not important, plus start and end breakets
    val trimmed =
        this.filterNot { s -> (s == ' ') || (s == '\n') }.filterNot { s -> (s == '(') || (s == ')') }

    if (trimmed.contains('.'))
    {
        //upper part of decimal fraction is long number
        val upper = trimmed.filterNot { s -> s == '.' }.toInt()

        //lower part is pow of ten, on which must divide upper to get start decimal fraction
        val lower = 10.pow( trimmed.substringAfter('.').length)

        return Fraction(_upper = upper, _lower = lower)
    }

    return if (trimmed.contains('/'))
    {
        val fraction = trimmed.fields("/")
        Fraction(_upper = fraction[0].toInt(), _lower = fraction[1].toInt())
    }
    else Fraction(_upper = trimmed.toInt(), _lower = 1)
}

fun String.toFractionOrNull() : Fraction?
{
    if (isNotFraction()) return null
    else return toFraction()
}