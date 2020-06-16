package com.dev.smurf.highmathcalculator.StringsExtension

import android.util.Log
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.Numbers.Fraction
import com.dev.smurf.highmathcalculator.Numbers.zero

fun gcd(a: Int, b: Int): Int
{
    if (b == 0) return a
    else return gcd(b, a % b)
}

fun gcdLong(a: Long, b: Long): Long
{
    if (b == zero) return a
    else return gcdLong(b, a % b)
}


fun Int.pow(x: Int): Int
{
    if (x == 0) return 1
    var res: Int = 1
    for (i in 1..x)
        res *= this
    return res
}

fun Long.pow(x: Long): Long
{
    if (x == 0L) return 1
    var res = 1L
    for (i in 1..x)
        res *= this
    return res
}

fun String.countLines(): Int
{
    var counter = 0
    for (i in this) if (i == '\n') counter++
    return counter + 1
}

fun String.countWords(): Int
{
    var counter = 0
    for (i in this) if (i == ' ') counter++
    return counter + 1
}

fun Int.factorial(): Int
{
    if (this < 0) throw Exception("FactorialBelowZeroException")
    var res = 1
    var tmp = this
    while (tmp > 0)
    {
        res *= tmp
        tmp--
    }
    return res
}

fun binCofs(n: Int, k: Int) = ((n.factorial()) / (k.factorial() * (n - k).factorial()))

fun Int.comb(leng: Int): Int
{
    var res = 0
    var counter = 1
    for (i in 0 until leng)
    {
        res = (res * 10) + counter
        counter++
    }
    return res
}

fun Int.equals(other: Fraction): Boolean
{
    return if ((other.upper % other.lower) == zero) ((other.upper / other.lower) == this.toLong())
    else false
}

fun Long.equals(other : Fraction): Boolean
{
    return if ((other.upper % other.lower) == zero) ((other.upper / other.lower) == this)
    else false
}

//use numberToDergeeForm if you need to translate only numbers
fun String.toDegree(): String
{
    var res = ""
    for (i in this)
    {
        when (i)
        {
            '0' -> res += '⁰'
            '1' -> res += '¹'
            '2' -> res += '²'
            '3' -> res += '³'
            '4' -> res += '⁴'
            '5' -> res += '⁵'
            '6' -> res += '⁶'
            '7' -> res += '⁷'
            '8' -> res += '⁸'
            '9' -> res += '⁹'
            else -> throw Exception("UnknownSymbolInDegree")
        }
    }

    return res
}

fun String.numbersToDegreeForm(): String
{
    var res = ""
    for (i in this)
    {
        when (i)
        {
            '0' -> res += '⁰'
            '1' -> res += '¹'
            '2' -> res += '²'
            '3' -> res += '³'
            '4' -> res += '⁴'
            '5' -> res += '⁵'
            '6' -> res += '⁶'
            '7' -> res += '⁷'
            '8' -> res += '⁸'
            '9' -> res += '⁹'
            else -> res += i
        }
    }

    return res
}

fun String.toNumber(): String
{
    var res = ""

    for (i in this)
    {
        when (i)
        {
            '⁰' -> res += '0'
            '¹' -> res += '1'
            '²' -> res += '2'
            '³' -> res += '3'
            '⁴' -> res += '4'
            '⁵' -> res += '5'
            '⁶' -> res += '6'
            '⁷' -> res += '7'
            '⁸' -> res += '8'
            '⁹' -> res += '9'
            else -> throw Exception("UnknownSymbolInDegree")
        }
    }

    return res
}

fun String.translateDegree(): String
{
    var res = ""

    for (i in this)
    {
        when (i)
        {
            '⁰' -> res += '0'
            '¹' -> res += '1'
            '²' -> res += '2'
            '³' -> res += '3'
            '⁴' -> res += '4'
            '⁵' -> res += '5'
            '⁶' -> res += '6'
            '⁷' -> res += '7'
            '⁸' -> res += '8'
            '⁹' -> res += '9'
            else -> res += i
        }
    }

    return res
}

fun String.degreesToNormalForm(): String
{
    var result = ""
    for (i in this)
    {
        if (i.isDegree())
        {
            result += i.toString().translateDegree()
        }
        else
        {
            result += i
        }
    }
    return result
}

fun Char.isDegree(): Boolean
{
    when (this)
    {
        '⁰' -> return true
        '¹' -> return true
        '²' -> return true
        '³' -> return true
        '⁴' -> return true
        '⁵' -> return true
        '⁶' -> return true
        '⁷' -> return true
        '⁸' -> return true
        '⁹' -> return true
        else -> return false
    }
}

fun String.amountOfCofsInPolinom(): Int
{
    var counter = 1
    var flagOfClose = true
    for (i in this)
    {
        when (i)
        {
            '(' -> flagOfClose = false
            ')' -> flagOfClose = true
            '+' -> if (flagOfClose) counter++
            '-' -> if (flagOfClose) counter++
            else -> false
        }
    }
    return counter
}

fun String.removePluses(): String
{
    var result = ""
    var flagOfClose = true
    for (i in this)
    {
        when (i)
        {
            '(' ->
            {
                flagOfClose = false
                result += i
            }
            ')' ->
            {
                flagOfClose = true
                result += i
            }
            '+' ->
            {
                if (!flagOfClose) result += i
                else result += ' '
            }

            else -> result += i
        }
    }
    return result
}

//returns position of first minus or plus which is separating polynomial cofs in parts or -1
fun String.getFirstCofSeparatorPosition(): Int
{
    var flagOfClose = true
    for (i in this.indices)
    {
        when (this[i])
        {
            '(' -> flagOfClose = false
            ')' -> flagOfClose = true
            '+' -> if (flagOfClose && i != 0) return i
            '-' -> if (flagOfClose && i != 0) return i
        }
    }
    return -1
}


fun String.substringBeforeSymbol(): String
{
    for (i in this)
    {
        if ((i in 'a'..'z') || (i in 'A'..'Z'))
        {
            return this.substringBefore(i)
        }
    }
    return this
}

fun String.substringBeforeSymbol(isNotSymbol: Char): String
{
    for (i in this)
    {
        if (((i in 'a'..'z') || (i in 'A'..'Z')) && i != isNotSymbol)
        {
            return this.substringBefore(i)
        }
    }
    return this
}

fun String.separateBySymbol(symbol: String = " "): Array<String>
{
    val result: Array<String> = arrayOf()
    var tmpString = this

    while (tmpString.contains(symbol))
    {
        result.plus(tmpString.substringBefore(symbol))

        tmpString = tmpString.substringAfter(symbol)
    }

    return result
}

fun String.substringAfterSymbolIncluded(): String
{
    for (i in this)
    {
        if ((i in 'a'..'z') || (i in 'A'..'Z'))
        {
            return (i + this.substringAfter(i))

        }
    }
    return ""
}

fun String.substringAfterSymbolIncluded(isNotSymbol: Char): String
{
    for (i in this)
    {
        if (((i in 'a'..'z') || (i in 'A'..'Z')) && (i != isNotSymbol))
            {
                return (i + this.substringAfter(i))

            }
    }
    return ""
}

fun String.substringAfterSymbol(): String
{
    for (i in this)
    {
        if ((i in 'a'..'z') || (i in 'A'..'Z'))
        {
            return this.substringAfter(i)

        }
    }
    return ""
}

fun String.substringAfterSymbol(isNotSymbol: Char): String
{
    for (i in this)
    {
        if (((i in 'a'..'z') || (i in 'A'..'Z')) && (i != isNotSymbol))
        {
            return this.substringAfter(i)

        }
    }
    return this
}

//returns MuableList of strings get from separation of string by str
fun String.fields(breakString: String): MutableList<String>
{
    val result = mutableListOf<String>()

    var str = this

    if (!str.contains(breakString))
    {
        result.add(str.substringBefore(breakString))

        return result
    }
    do
    {
        result.add(str.substringBefore(breakString))
        str = str.substringAfter(breakString)
    } while (str.contains(breakString))

    result.add(str.substringBefore(breakString))

    return result
}





