package com.example.smurf.mtarixcalc



class complexNumber(private var re : fraction = fraction(), private  var im : fraction = fraction())
{

    operator  fun plus (secNumber : Any? ):complexNumber
    {
        when(secNumber)
        {
            is complexNumber ->
            {
                return complexNumber(this.re + secNumber.re , this.im + secNumber.im)
            }
            is Int ->
            {
                return complexNumber(this.re +secNumber , this.im)
            }
            is fraction ->
            {
                return complexNumber(this.re + secNumber , this.im)
            }
            else -> throw Exception("Unknown type")
        }
    }


    operator fun times(secNumber : Any?): complexNumber
    {
        when(secNumber)
        {
            is complexNumber->
            {
                val newNum = complexNumber( (this.re * secNumber.re - this.im*secNumber.im) , (this.re * secNumber.im - secNumber.re * this.im))
                return newNum
            }
            is Int ->
            {
                return complexNumber(this.re * secNumber , this.im*secNumber)
            }
            is fraction ->
            {
                return complexNumber(this.re * secNumber , this.im*secNumber)
            }
            else -> throw Exception("Unknown type")
        }
    }

    operator fun minus(secNumber: Any?) :complexNumber
    {
        when(secNumber)
        {
            is complexNumber->
            {
                return  complexNumber( this.re - secNumber.re , this.im - secNumber.im)

            }
            is Int ->
            {
                return complexNumber(this.re - secNumber , this.im)
            }
            is fraction ->
            {
                return complexNumber(this.re - secNumber , this.im)
            }
            else -> throw Exception("Unknown type")
        }
    }

    operator  fun div( secNumber: Any?) : complexNumber
    {
        when(secNumber)
        {
            is complexNumber ->
            {
                return complexNumber( ( (this.re * secNumber.re + this.im*secNumber.im) / (secNumber.re*secNumber.re + secNumber.im*secNumber.im) ) ,
                    ( (secNumber.re*this.im - this.re*secNumber.im) / (secNumber.re*secNumber.re + secNumber.im*secNumber.im) ) )
            }
            is Int ->
            {
                return complexNumber(this.re / secNumber , this.im / secNumber)
            }
            is fraction ->
            {
                return complexNumber(this.re / secNumber , this.im / secNumber)
            }
            else -> throw Exception("Unknown type")
        }

    }

    fun pow( Pow : Int) : complexNumber
    {
        var newNum : complexNumber = this
        if( Pow == 0)return complexNumber(fraction(1))
        for( i in 1..Pow)
        {
            newNum *= this
        }
        return newNum
    }

    override operator fun equals(other : Any?) : Boolean
    {
        when(other) {
            is complexNumber -> return ((this.re == other.re) && (this.im == other.im))
            is fraction -> return (this.re == other)&&(this.im == fraction())
            is Int -> return (this.re == other)&&(this.im == fraction())
            is Double -> return(this.re == other)&&(this.im == fraction())
            is Float -> return(this.re == other)&&(this.im == fraction())
            is String -> return(this == other.toComplex())
            else -> return false
        }
    }


    override fun toString(): String
    {
        when
        {
            //вещественная часть 0 возвращаем только действительную
            im == fraction() -> return re.toString()
            //действительная часть 0 возарвщвем только мнимую
            re == fraction() -> return im.toString() + 'i'

            im < fraction() -> return (re.toString() + im.toString() + 'i')

            else -> return (re.toString() + '+' + im.toString() + 'i')

        }
    }

}

