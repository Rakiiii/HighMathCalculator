package com.dev.smurf.highmathcalculator.Numbers

import com.dev.smurf.highmathcalculator.Exceptions.WrongTypeForOperationException
import com.dev.smurf.highmathcalculator.StringsExtension.toComplexNumber


class ComplexNumber( _re : Fraction = Fraction(),_im : Fraction = Fraction())
{

    var re = _re
        private set
    var im = _im
        private set

    operator  fun plus (secNumber : Any? ): ComplexNumber
    {
        when(secNumber)
        {
            is ComplexNumber ->
            {
                return ComplexNumber(this.re + secNumber.re , this.im + secNumber.im)
            }
            is Int ->
            {
                return ComplexNumber(this.re +secNumber , this.im)
            }
            is Fraction ->
            {
                return ComplexNumber(this.re + secNumber , this.im)
            }
            else -> throw WrongTypeForOperationException("plus")
        }
    }


    operator fun times(secNumber : Any?): ComplexNumber
    {
        when(secNumber)
        {
            is ComplexNumber ->
            {
                val newNum = ComplexNumber( (this.re * secNumber.re - this.im*secNumber.im) , (this.re * secNumber.im - secNumber.re * this.im))
                return newNum
            }
            is Int ->
            {
                return ComplexNumber(this.re * secNumber , this.im*secNumber)
            }
            is Fraction ->
            {
                return ComplexNumber(this.re * secNumber , this.im*secNumber)
            }
            else -> throw WrongTypeForOperationException("times")
        }
    }

    operator fun minus(secNumber: Any?) : ComplexNumber
    {
        when(secNumber)
        {
            is ComplexNumber ->
            {
                return  ComplexNumber( this.re - secNumber.re , this.im - secNumber.im)

            }
            is Int ->
            {
                return ComplexNumber(this.re - secNumber , this.im)
            }
            is Fraction ->
            {
                return ComplexNumber(this.re - secNumber , this.im)
            }
            else -> throw WrongTypeForOperationException("minus")
        }
    }

    operator  fun div( secNumber: Any?) : ComplexNumber
    {
        when(secNumber)
        {
            is ComplexNumber ->
            {
                return ComplexNumber( ( (this.re * secNumber.re + this.im*secNumber.im) / (secNumber.re*secNumber.re + secNumber.im*secNumber.im) ) ,
                    ( (secNumber.re*this.im - this.re*secNumber.im) / (secNumber.re*secNumber.re + secNumber.im*secNumber.im) ) )
            }
            is Int ->
            {
                return ComplexNumber(this.re / secNumber , this.im / secNumber)
            }
            is Fraction ->
            {
                return ComplexNumber(this.re / secNumber , this.im / secNumber)
            }
            else -> throw WrongTypeForOperationException("division")
        }

    }

    fun pow( Pow : Int) : ComplexNumber
    {
        var newNum : ComplexNumber = this
        if( Pow == 0)return ComplexNumber(Fraction(1))
        for( i in 1..Pow)
        {
            newNum *= this
        }
        return newNum
    }

    override operator fun equals(other : Any?) : Boolean
    {
        when(other) {
            is ComplexNumber -> return ((this.re == other.re) && (this.im == other.im))
            is Fraction -> return (this.re == other)&&(this.im == Fraction())
            is Int -> return (this.re == other)&&(this.im == Fraction())
            is Double -> return(this.re == other)&&(this.im == Fraction())
            is Float -> return(this.re == other)&&(this.im == Fraction())
            is String -> return(this == other.toComplexNumber())
            else -> return false
        }
    }


    override fun toString(): String
    {
        when
        {
            //вещественная часть 0 возвращаем только действительную
            im == Fraction() -> return re.toString()
            //действительная часть 0 возарвщвем только мнимую
            re == Fraction() -> return im.toString() + 'i'

            im < Fraction() -> return ( '(' + re.toString() + im.toString() + 'i' + ')')

            else -> return ('('+re.toString() + '+' + im.toString() + 'i'+')')

        }
    }

    fun isReal() = (im == Fraction())

    fun isImagination() = ( re == Fraction())

    fun length() : Int
    {
        if(isReal())return re.maxLenght()
        if(isImagination())return im.maxLenght()+1
        var result = re.maxLenght() + im.maxLenght() + 3
        if(!im.isBeloweZero())result++
        return result
    }

    fun isBelowZero() = (re.upper < 0 && isReal())

    fun Copy() : ComplexNumber
    {
        return ComplexNumber( _re = re.Copy(),_im = im.Copy())
    }

    fun containsFractions() = (!re.isInt() || !im.isInt())

}

