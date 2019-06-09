package com.example.smurf.mtarixcalc

import android.util.Log

fun gcd( a : Int , b : Int) : Int
{
    if(b == 0)return a
    else return gcd( b ,a % b )
}

fun String.toFraction():fraction
{
    when
    {
        (this.contains('.'))->return fraction(this.filterNot{s -> (s == '.' )}.filterNot { s -> (s ==')' && (s == '(')) }.toInt() ,
            10.pow( this.substringAfter('.').filterNot { s -> (s == ')') }.length))
        (this.contains('/')) -> return fraction(this.substringBefore('/').filterNot { s -> (s == '(') }.toInt(),
            this.substringAfter('/').filterNot { s -> (s == ')') }.toInt())
        else -> return fraction(this.filterNot { s -> (s == '(' && s == ')' ) }.toInt(), 1)
    }
}

fun Int.pow(x : Int) : Int
{
    if (x == 0)return 1
    var res : Int = 1
    for( i in 1..x)
        res *=this
    return res
}

fun String.translateToComplex(signIm : Char = '+' , signRe : Char = '+'  ) : complexNumber
{
    var line = this
    if((signIm != '+' && signIm != '-') || (signRe != '+' && signRe != '-'))throw Exception("unnown char in complex amountOfRoots translation")
    //разбиваем текущий столбец на отдельные цифры
    var subIm = fraction()
    if(line.substringAfter(signIm).filterNot { s->(s == 'i') }.isBlank())subIm = fraction(1,1)
    else subIm = line.substringAfter(signIm).filterNot { s -> (s == 'i') }.toFraction()
    var subRe = line.substringBefore(signIm).toFraction()

    if(signRe == '-')subRe*=fraction(-1,1)
    if(signIm == '-')subIm*= fraction(-1,1)
    return complexNumber(subRe , subIm)
}

fun String.toComplex() : complexNumber
{
    var line = this
    var signRe = '+'
    if(line[0] == '-')
    {
        signRe = '-'
        line = line.substringAfter('-')
    }
    when
    {
        line.contains('+') && line.contains('i', true) ->
        {
            return line.translateToComplex('+' , signRe )
        }
        line.contains('-') && line.contains('i')->
        {
            return line.translateToComplex('-' , signRe)
        }
        line.contains('i')->
        {
            var subIm = fraction()
            if(line.filterNot { s->(s == 'i') }.isBlank())subIm = fraction(1,1)
            else subIm = line.filterNot { s -> (s == 'i') }.toFraction()
            if(signRe == '-')subIm*= fraction(-1,1)
            return complexNumber(im = subIm)
        }
        else->
        {
            var subRe = line.toFraction()
            if(signRe == '-')subRe *= fraction(-1,1)
            return complexNumber(re = subRe)
        }
    }
}

fun String.countLines() : Int
{
    var counter = 0
    for(i in this)if(i == '\n')counter++
    return counter + 1
}

fun String.countWords() : Int
{
    var counter = 0
    for( i in this)if( i == ' ')counter ++
    return counter + 1
}

fun Int.factorial() : Int
{
    if(this < 0)throw Exception("FactorialBelowZeroException")
    var res = 1
    var tmp = this
    while(tmp > 0)
    {
        res *=tmp
        tmp --
    }
    return res
}

fun binCofs(n : Int , k : Int) = ( ( n.factorial() ) / ( k.factorial() * ( n - k ).factorial() ) )

fun Int.comb( leng : Int) : Int
{
    var res = 0
    var counter = 1
    for(i in 0 until leng)
    {
        res = (res*10) + counter
        counter ++
    }
    return res
}

fun Int.equals (other : fraction) : Boolean
{
    if ( (other.upper % other.lower) == 0) return ( (other.upper/other.lower) == this)
    else return false
}

fun String.toDegree():String
{
    var res = ""
    for( i in this)
    {
        when(i)
        {
            '0'->res += '⁰'
            '1'->res += '¹'
            '2'->res += '²'
            '3'->res += '³'
            '4'->res += '⁴'
            '5'->res += '⁵'
            '6'->res += '⁶'
            '7'->res += '⁷'
            '8'->res += '⁸'
            '9'->res += '⁹'
            else ->throw Exception("UnknownSymbolInDegree")
        }
    }

    return res
}

fun String.toNumber():String
{
    var res =""

    for( i in this)
    {
        when(i)
        {
            '⁰'->res += '0'
            '¹'->res += '1'
            '²'->res += '2'
            '³'->res += '3'
            '⁴'->res += '4'
            '⁵'->res += '5'
            '⁶'->res += '6'
            '⁷'->res += '7'
            '⁸'->res += '8'
            '⁹'->res += '9'
            else ->throw Exception("UnknownSymbolInDegree")
        }
    }

    return res
}

fun String.translateDegree():String
{
    var res =""

    for( i in this)
    {
        when(i)
        {
            '⁰'->res += '0'
            '¹'->res += '1'
            '²'->res += '2'
            '³'->res += '3'
            '⁴'->res += '4'
            '⁵'->res += '5'
            '⁶'->res += '6'
            '⁷'->res += '7'
            '⁸'->res += '8'
            '⁹'->res += '9'
            else ->res += i
        }
    }

    return res
}

fun String.AmountOfCofsInPolinom() : Int
{
    var counter = 0
    var flagOfClose = true
    for( i in this)
    {
        when(i)
        {
            '('-> flagOfClose = false
            ')'->flagOfClose = true
            '+'->if(flagOfClose)counter++
            else->false
        }
    }
    return counter
}

fun String.removePluses() : String
{
    var result = ""
    var flagOfClose = true
    for( i in (this as CharSequence) )
    {
        when(i)
        {
            '('->
            {
                flagOfClose = false
                result += i
            }
            ')'->
            {
                flagOfClose = true
                result += i
            }
            '+'->
            {
                if(!flagOfClose) result += i
                else result += ' '
            }

            else->result += i
        }
    }
    return result
}

fun String.toPolinomCofsString( symb : Char) : String
{
    var tmp = this.translateDegree().removePluses() + symb + '0' + ' '
    var finalString = tmp.substringBefore(' ').substringBefore(symb).substringAfter('(').substringBefore(')') + ' '
    Log.d("DEGUB@" , "Init Strings")
    for(i in 0 until this.AmountOfCofsInPolinom())
    {
        Log.d("DEBUG@" , "Cycle Starts _" + this.AmountOfCofsInPolinom().toString())
        //throw Exception("Cycle started")
        var word1 = tmp.substringBefore(' ')
        var word2 = tmp.substringAfter(' ').substringBefore(' ')
        Log.d("DEBUG@" , "Words init _" + i.toString() )
        for( j in 0 until (word1.substringAfter(symb).toInt() - word2.substringAfter(symb).toInt()))
        {
            if( j == (word1.substringAfter(symb).toInt() - word2.substringAfter(symb).toInt() - 1 ) )
            {
                finalString += word2.substringBefore(symb).substringAfter('(').substringBefore(')') + ' '
            }
            else
            {
                finalString += "0 "
            }
        }

        Log.d("DEBUG@" , "finnile word after _" +i.toString() + ' ' + finalString)

        tmp = tmp.substringAfter(' ')

        Log.d("DEBUG@" , "tmp after_" + i.toString() + tmp)
    }

    Log.d("DEBUG@" , "Cycle finished")

    return finalString
}