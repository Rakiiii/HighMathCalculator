package com.dev.smurf.highmathcalculator.Numbers

import com.dev.smurf.highmathcalculator.Exceptions.DivisionFractionByZeroException
import com.dev.smurf.highmathcalculator.Exceptions.WrongTypeForOperationException
import com.dev.smurf.highmathcalculator.StringsExtension.gcd
import com.dev.smurf.highmathcalculator.StringsExtension.gcdLong
import kotlin.math.absoluteValue
const val zero = 0L
class Fraction(_upper : Long = 0, _lower : Long = 1)
{

    var upper = _upper
    private set

    var lower = if(_lower != zero)_lower else 1L
    private set

    operator fun plus(right : Any?) : Fraction
    {
        when(right)
        {
            is Fraction ->
            {
                if(right.upper == zero)return this
                if(this.upper == zero)return right
                val res = Fraction ( this.upper*right.lower + this.lower*right.upper , this.lower*right.lower)
                if(res.lower < zero)
                {
                    res.upper *= -1
                    res.lower *= -1
                }
                return res.cut()
            }
            is Int ->
            {
                return Fraction(this.upper+right*this.lower , this.lower)
            }
            else -> throw WrongTypeForOperationException("plus")
        }
    }

    operator fun minus(right : Any?) : Fraction
    {
        when(right)
        {
            is Fraction ->
            {
                if (right.upper == zero) return this
                if (this.upper == zero)
                    return Fraction( _upper = - right.upper , _lower = right.lower)
                val res = Fraction(this.upper * right.lower - this.lower * right.upper, this.lower * right.lower)
                if (res.lower < 0) {
                    res.upper *= -1
                    res.lower *= -1
                }
                return res.cut()
            }
            is Int -> return Fraction(this.upper-right*this.lower , this.lower)
            else -> throw WrongTypeForOperationException("minus")
        }
    }

    operator fun div(right: Any?) : Fraction
    {
        when(right)
        {
            is Fraction ->
            {

                if (this.upper == zero) return Fraction()
                if (right.upper == zero) throw DivisionFractionByZeroException()
                val res = Fraction(this.upper * right.lower, this.lower * right.upper)
                if (res.lower < zero) {
                    res.upper *= -1
                    res.lower *= -1
                }
                return res.cut()
            }
            is Int -> return Fraction(this.upper , this.lower*right)
            else -> throw WrongTypeForOperationException("division")
        }
    }

    operator fun times(right: Any?) : Fraction
    {
        when (right)
        {
            is Fraction ->
            {


                if (this.upper == zero || right.upper == zero) return Fraction()
                val res = Fraction(this.upper * right.upper, this.lower * right.lower)
                if (res.lower < zero) {
                    res.upper *= -1
                    res.lower *= -1
                }
                return res.cut()
            }
            is Int -> return Fraction(this.upper*right , this.lower)
            else -> throw WrongTypeForOperationException("times")
        }
    }

    override operator fun equals(other : Any?) : Boolean
    {
        when(other)
        {
            is Fraction ->
            {
                if(this.upper == zero && other.upper == zero)return true
                else return (this.upper == other.upper && this.lower == other.lower)
            }
            is Int ->
            {
                if ( (this.upper % this.lower) == zero) return ( (this.upper/this.lower) == other )
                else return false
            }
            is Double ->
            {
                val check : Double = (this.upper.toDouble() /  this.lower.toDouble() )
                return (check == other)
            }
            is Float ->
            {
                val check : Float = ( this.upper.toFloat() / this.lower.toFloat() )
                return (check == other)
            }
            else -> return false
        }
    }

    override fun toString(): String
    {
        if (lower == 1L)return upper.toString()
        else
        {
            if(upper >= 0) return( "(" + upper.toString() + "/" + lower.toString() + ")" )
            else return("-" + "(" + (-1*upper).toString() + "/" + lower.toString() + ")" )
        }
    }

    operator fun compareTo(other : Fraction) : Int
    {
        if( this == other )return 0
        else
        {
            when
            {
                ( other == Fraction() )->if(this.upper > zero)return 1 else return -1
                (this.upper >= other.upper && this.lower <= other.lower)->return 1
                (this.upper <= other.upper && this.lower >= other.lower)->return -1
                else -> if ( ( this.upper.toDouble() / this.lower.toDouble() ) > (other.upper.toDouble() / other.lower.toDouble() ) ) return 1
                    else return -1
            }
        }
    }


    private fun cut() : Fraction
    {
        val _gcd = gcdLong(
            this.upper.absoluteValue,
            this.lower.absoluteValue
        )
        this.upper /= _gcd
        this.lower /= _gcd
        return this
    }


    fun maxLenght() : Int
    {
        if(upper.absoluteValue.toString().length > lower.absoluteValue.toString().length)
            return upper.absoluteValue.toString().length
        else
            return lower.absoluteValue.toString().length
    }

    fun isInt() = (lower == 1L)

    fun isBeloweZero() = (upper < 0)

    fun Copy() : Fraction
    {
        return Fraction( _upper = upper , _lower = lower)
    }

    fun isDecimal() : Boolean
    {
        var test = lower
        while (test % 10 == zero)
        {
            test /= 10
        }
        return test == 1L
    }
}

