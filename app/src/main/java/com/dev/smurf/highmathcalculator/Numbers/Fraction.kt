package com.dev.smurf.highmathcalculator.Numbers

import com.dev.smurf.highmathcalculator.Exceptions.WrongDataException
import com.dev.smurf.highmathcalculator.Utils.gcd
import kotlin.math.absoluteValue

class Fraction(_upper : Int = 0, _lower : Int = 1)
{

    var upper = _upper
    private set

    var lower = _lower
    private set

    operator fun plus(right : Any?) : Fraction
    {
        when(right)
        {
            is Fraction ->
            {
                if(right.upper == 0)return this
                if(this.upper == 0)return right
                val res = Fraction ( this.upper*right.lower + this.lower*right.upper , this.lower*right.lower)
                if(res.lower < 0)
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
            else -> throw WrongDataException("Unknown type for plus operation")
        }
    }

    operator fun minus(right : Any?) : Fraction
    {
        when(right)
        {
            is Fraction ->
            {
                if (right.upper == 0) return this
                if (this.upper == 0)
                    return Fraction( _upper = - right.upper , _lower = right.lower)
                val res = Fraction(this.upper * right.lower - this.lower * right.upper, this.lower * right.lower)
                if (res.lower < 0) {
                    res.upper *= -1
                    res.lower *= -1
                }
                return res.cut()
            }
            is Int -> return Fraction(this.upper-right*this.lower , this.lower)
            else -> throw WrongDataException("Unknown type")
        }
    }

    operator fun div(right: Any?) : Fraction
    {
        when(right)
        {
            is Fraction ->
            {

                if (this.upper == 0) return Fraction()
                if (right.upper == 0) throw WrongDataException("Can't divide by zero")
                val res = Fraction(this.upper * right.lower, this.lower * right.upper)
                if (res.lower < 0) {
                    res.upper *= -1
                    res.lower *= -1
                }
                return res.cut()
            }
            is Int -> return Fraction(this.upper , this.lower*right)
            else -> throw WrongDataException("Unknown type")
        }
    }

    operator fun times(right: Any?) : Fraction
    {
        when (right)
        {
            is Fraction ->
            {


                if (this.upper == 0 || right.upper == 0) return Fraction()
                val res = Fraction(this.upper * right.upper, this.lower * right.lower)
                if (res.lower < 0) {
                    res.upper *= -1
                    res.lower *= -1
                }
                return res.cut()
            }
            is Int -> return Fraction(this.upper*right , this.lower)
            else -> throw WrongDataException("Unknown type")
        }
    }

    override operator fun equals(other : Any?) : Boolean
    {
        when(other)
        {
            is Fraction ->
            {
                if(this.upper == 0 && other.upper == 0)return true
                else return (this.upper == other.upper && this.lower == other.lower)
            }
            is Int ->
            {
                if ( (this.upper % this.lower) == 0) return ( (this.upper/this.lower) == other )
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
        if (lower == 1)return upper.toString()
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
                ( other == Fraction() )->if(this.upper > 0)return 1 else return -1
                (this.upper >= other.upper && this.lower <= other.lower)->return 1
                (this.upper <= other.upper && this.lower >= other.lower)->return -1
                else -> if ( ( this.upper.toDouble() / this.lower.toDouble() ) > (other.upper.toDouble() / other.lower.toDouble() ) ) return 1
                    else return -1
            }
        }
    }


    private fun cut() : Fraction
    {
        val _gcd = gcd(this.upper.absoluteValue , this.lower.absoluteValue)
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
        /*
        var str = this.toString()
        if( str.substringBefore('/').length > str.substringAfter('/').length )
        {
            //Log.d("debug@" , str.substringBefore('/').length.toString())
            return str.substringBefore('/').length
        }
        else
        {
            //Log.d("debug@" , str.substringAfter('/').length.toString())
            return str.substringAfter('/').length
        }*/
    }

    fun isInt() = (lower == 1)

    fun isBeloweZero() = (upper < 0)

    fun Copy() : Fraction
    {
        return Fraction( _upper = upper , _lower = lower)
    }
}

