package com.dev.smurf.highmathcalculator.Polynomials

import android.util.Log
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.StringsExtension.toDegree
import com.dev.smurf.highmathcalculator.Utils.minusToCof
import com.dev.smurf.highmathcalculator.Utils.plusToCof


fun exponensialRecursiveDivison(
    division: ArrayList<Pair<Int, ComplexNumber>>,
    divider: ArrayList<Pair<Int, ComplexNumber>>

): Pair<ArrayList<Pair<Int, ComplexNumber>>, ArrayList<Pair<Int, ComplexNumber>>>
{
    val result: ArrayList<Pair<Int, ComplexNumber>> = arrayListOf()

    //if degree of divione is smaller the degree of divider then stop
    while (division.last().first >= divider.last().first)
    {
        Log.d("div@","division ${division.String()}")
        //deferense between max degree of division and divider
        val degreeCof = division.last().first - division.last().first
        //cof of elem on this stage of division
        val cof = division.last().second / divider.last().second

        //add new result of this part to result
        result.plusToCof(degreeCof, cof)

        //calculate remainder
        for (i in divider)
        {
            division.minusToCof(i.first + degreeCof, i.second * cof)
        }

    }
    return Pair(result, division)
}

fun ArrayList<Pair<Int, ComplexNumber>>.String():String
{
    var result = ""
    this.map {
        result += it.second.toString()+"x^"+it.first.toString().toDegree()+"+"
    }
    return result
}