package com.dev.smurf.highmathcalculator.Utils

import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber

fun ArrayList<Pair<Int, ComplexNumber>>.containsDegree(degree : Int) : Int
{
    for(i in this.indices)
    {
        if(this[i].first == degree)return i
    }
    return -1
}

fun ArrayList<Pair<Int, ComplexNumber>>.plusToCof( degree: Int , value : ComplexNumber)
{
    val pos = this.containsDegree(degree )
    if ( pos != -1 )
    {
        this[pos] = Pair( degree , this[pos].second + value )
    }
    else
    {
        this.add( Pair(degree,value) )
        this.sortBy { s -> s.first }
        this.reverse()
    }
}

fun ArrayList<Pair<Int, ComplexNumber>>.minusToCof( degree: Int , value : ComplexNumber)
{
    val pos = this.containsDegree(degree )
    if ( pos != -1 )
    {
        this[pos] = Pair( degree , this[pos].second - value )
    }
    else
    {
        this.add( Pair( degree , value * (-1) ) )
        this.sortBy { s -> s.first }
        this.reverse()
    }
}