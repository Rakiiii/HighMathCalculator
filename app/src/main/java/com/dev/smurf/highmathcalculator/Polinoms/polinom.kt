package com.example.smurf.mtarixcalc

import android.widget.EditText

class polinom( _size : Int)
{
    val size : Int = _size

    private val cofs : Array<complexNumber> = Array(_size , { s -> complexNumber() })

    var roots : Array<complexNumber> = Array(size , { i-> complexNumber() } )
    private set

    //количество уже найденных корней
    var amountOfRoots : Int = 0
    private set

    constructor(pol : EditText):this(pol.text.toString().trim().countWords())
    {
        var cofLine = pol.text.toString().trim()
        for(i in  0 until size)
        {
            cofs[i] = cofLine.substringBefore(' ').toComplex()
            cofLine = cofLine.substringAfter(' ')
        }
    }

    constructor( _line : String):this(_line.countWords())
    {
        var line = _line.trim()
        for(i in 0 until size)
        {
            cofs[i] = line.substringBefore(' ').toComplex()
            line = line.substringAfter(' ')
        }
    }


    constructor(matrix : Matrix):this(matrix.width + 1)
    {
        cofs[0] = complexNumber(fraction(1,1))
        when(size)
        {
            3->
            {
                cofs[1] = (matrix.matrix[0][0]*-1) + (matrix.matrix[1][1]*-1)
                cofs[2] = matrix.determinant()
            }
            4->
            {
                cofs[1] = (matrix.matrix[0][0] * -1) + (matrix.matrix[1][1] * -1) + (matrix.matrix[2][2] * -1)
                cofs[2] = matrix.minor(0,0).determinant()+
                          matrix.minor(1,1).determinant()+
                          matrix.minor(2,2).determinant()
                cofs[3] = matrix.determinant() * -1
            }
            else->
            {
                throw Exception("Matrix is too big")

            }
        }
    }

    constructor( pol : polinom):this(pol.size)
    {
        for(i in 0 until size)
        {
            cofs[i] = pol.cofs[i]
        }
    }


    operator fun compareTo(other: Any?):Int
    {
        when(other)
        {
            is polinom ->
            {
                when
                {
                    other.cut().size >= this.cut().size -> return 1
                    else -> return -1
                }
            }
            else -> throw Exception("Unknown type for compare")
        }
    }

    operator fun plus( other : Any?) : polinom
    {
        when(other)
        {
            is polinom ->
            {
                if (other.size >= this.size)
                {
                    val res = polinom(other)
                    for (i in 0 until size) res.cofs[res.size - i - 1] = res.cofs[res.size - i - 1] + cofs[size - i - 1]
                    return res
                }
                else
                {
                    val res = polinom(this)
                    for(i in 0 until other.size)res.cofs[res.size - i - 1] = res.cofs[res.size - i - 1] + other.cofs[other.size - i - 1]
                    return res
                }
            }
            is complexNumber ->
            {
                val res = polinom(this)
                res.cofs[size] = res.cofs[size] + other
                return res
            }
            is Int ->
            {
                val res = polinom(this)
                res.cofs[size] = res.cofs[size] + other
                return res
            }
            is fraction->
            {
                val res = polinom(this)
                res.cofs[size] = res.cofs[size] + other
                return res
            }
            else ->
            {
                throw Exception("Unnknown type for polinom plus")
            }
        }
    }

    operator fun minus( other : Any?) : polinom
    {
        when(other)
        {
            is polinom ->
            {
                if (other.size >= this.size)
                {
                    val res: polinom = polinom(other)
                    for (i in 0 until size) res.cofs[res.size - i - 1] =  cofs[size -1 - i] - res.cofs[res.size - i - 1]
                    return res
                }
                else
                {
                    val res = polinom(this)
                    for(i in 0 until other.size)res.cofs[res.size - i - 1] = res.cofs[res.size - i - 1] - other.cofs[other.size - 1 - i]
                    return res
                }
            }
            is complexNumber ->
            {
                val res = polinom(this)
                res.cofs[size] = res.cofs[size] - other
                return res
            }
            is Int ->
            {
                val res = polinom(this)
                res.cofs[size] = res.cofs[size] - other
                return res
            }
            is fraction->
            {
                val res = polinom(this)
                res.cofs[size] = res.cofs[size] - other
                return res
            }
            else ->
            {
                throw Exception("Unnknown type for polinom minus")
            }
        }
    }

    operator fun div(other: Any?) : Array<polinom>
    {
        when(other)
        {
            is polinom ->
            {
                var res = polinom( this.size - other.size )
                var tmp = polinom(this)
                var i = 0
                while (tmp >= other)
                {
                    res.cofs[i] = cofs[i]/other.cofs[i]
                    tmp = tmp - res*other
                    i ++
                    tmp.cut()
                }
                return arrayOf(res.cut() , tmp)

            }
            is Int ->
            {
                var res = polinom(this)
                for (i in 0 until res.size)cofs[i] = cofs[i] / other
                return arrayOf(res , polinom(1))
            }
            is complexNumber ->
            {
                var res = polinom(this)
                for (i in 0 until res.size)cofs[i] = cofs[i] / other
                return arrayOf(res , polinom(1))
            }
            is fraction ->
            {
                var res = polinom(this)
                for (i in 0 until res.size)cofs[i] = cofs[i] / other
                return arrayOf(res , polinom(1))
            }
            else -> throw Exception("Unknown type for polinom division")

        }
    }

    operator fun times(other: Any?) : polinom
    {
        when( other )
        {
            is polinom->
            {
                var res : polinom = polinom(size+other.size - 1)
                //res = res + this
                for(j in 0 until other.size)
                {
                    for (i in 0 until size)
                    {
                        res.cofs[ i + j ] += cofs[i] * other.cofs[j]
                    }
                }
                return res
            }
            is Int ->
            {
                var res = polinom(this)
                for (i in 0 until res.size)cofs[i] = cofs[i] * other
                return res
            }
            is complexNumber ->
            {
                var res = polinom(this)
                for (i in 0 until res.size)cofs[i] = cofs[i] * other
                return res
            }
            is fraction ->
            {
                var res = polinom(this)
                for (i in 0 until res.size)cofs[i] = cofs[i] * other
                return res
            }
            else -> throw Exception("Unknown type for polinom operation")
        }
    }

    fun revers() : polinom
    {
        var res = polinom(size)
        for(i in 0 until size)res.cofs[i] = cofs[size - i]
        return res
    }

    //поиск корня перебором
    fun result()
    {
        //перебор всех числителей вещественной части
        for ( i in -100..100)
        {
            //перебов всех знаменателей вещественной части
            for(k in 0..100)
            {
                //перебор все числитетелей мнимой части
                for (j in -100..100)
                {
                    //перебор всех знаменателей мнимой части
                    for(l in  0..100)
                    {
                        if (rightPol(complexNumber( fraction(i,k), fraction(j,l) ) ) && amountOfRoots != size - 1)
                        roots[amountOfRoots++] = complexNumber( fraction(i,k) , fraction(j,l) )

                    }
                }
            }
        }
    }

    //проверка корня
    fun rightPol( test : complexNumber) : Boolean
    {
        var res = complexNumber()
        for ( i in 0 until size)
        {
            res+=this.cofs[i]*test.pow(size - i)
        }
        if(res == complexNumber() )return true
        else return false
    }

    fun cut() : polinom
    {
        var i = 0
        var counter = 0
        while( this.cofs[i] == complexNumber() )
        {
            counter ++
            i ++
        }
        var res = polinom(size - counter)
        for ( i in 0 until size)res.cofs[i] = this.cofs[i + counter]
        return res
    }

    fun toString( symb : Char) : String
    {
        var res : String = ""

        for(i in 0 until size)
        {

            if(i == size - 1) res += ('(' + cofs[i].toString() + ')' )
            else res += ('(' + cofs[i].toString() + ')' + symb + (size - i - 1).toString().toDegree() + '+' )

        }
        return res
    }

    override fun toString(): String
    {
        var res : String = ""

        for(i in 0 until size)
        {
            if(i == size - 1) res += ('(' + cofs[i].toString() + ')' )
            else res += ('(' + cofs[i].toString() + ')' + 'x' + (size - i - 1).toString().toDegree() + '+' )
        }
        return res
    }



    fun stringWithRoots(symb : Char = 'x') : String
    {
        var string = ""
        for(i in 0 until amountOfRoots)
        {
            string += symb.toString() + '=' + roots[i].toString() + ',' + ' '
        }
        return string
    }
}