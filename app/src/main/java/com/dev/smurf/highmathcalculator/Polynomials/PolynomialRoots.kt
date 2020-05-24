package com.dev.smurf.highmathcalculator.Polynomials

import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.StringsExtension.separateBySymbol
import com.dev.smurf.highmathcalculator.StringsExtension.toComplexNumber

class PolynomialRoots
{

    private lateinit var roots: ArrayList<Pair<String, ComplexNumber>>

    constructor(){ roots = arrayListOf()}

    constructor( _roots: ArrayList<Pair<String, ComplexNumber>> = arrayListOf())
    {
        this.roots = _roots
    }

    constructor( string : String)
    {
        val tmp = string.separateBySymbol()

        for( i in tmp )
        {
            roots.add( Pair( i.substringBefore('=') , i.substringAfter('=').toComplexNumber() ) )
        }
    }

    fun addNewRoot(cof: String, root: ComplexNumber)
    {
        roots.add(Pair(cof, root))
    }

    fun getSepareteSymbol():String
    {
        if( roots[1].first == ",")return " "
        else return "="
    }

    override fun toString(): String
    {
        var string = ""

        for( i in roots)
        {
            string += i.first + "=" + i.second.toString() + " "
        }

        return string
    }

    operator fun iterator() = roots.iterator()

    /*class iterator(private val pol: PolynomialRoots)
    {
        var pos = 0

        fun next(): Pair<String, ComplexNumber>
        {
            pos++
            return pol.roots[pos]
        }

        fun hasNext() = (pos + 1 < pol.roots.size)
    }*/
}