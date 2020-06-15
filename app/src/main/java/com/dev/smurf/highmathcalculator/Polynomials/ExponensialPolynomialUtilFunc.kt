package com.dev.smurf.highmathcalculator.Polynomials

import android.util.Log
import com.dev.smurf.highmathcalculator.Numbers.ComplexNumber
import com.dev.smurf.highmathcalculator.StringsExtension.toDegree
import com.dev.smurf.highmathcalculator.Utils.minusToCof
import com.dev.smurf.highmathcalculator.Utils.plusToCof
import kotlinx.coroutines.delay


fun exponensialRecursiveDivison(
    div: MutableList<Pair<Int, ComplexNumber>>,
    divider: MutableList<Pair<Int, ComplexNumber>>

): Pair<MutableList<Pair<Int, ComplexNumber>>, MutableList<Pair<Int, ComplexNumber>>>
{
    val result: ArrayList<Pair<Int, ComplexNumber>> = arrayListOf()
    var division = div

    while (division.first().first >= divider.first().first)
    {
        Log.d("div@","division ${division.String()} result ${result.String()}")
        val maxDegree = division.first().first - divider.first().first

        val cof = division.first().second / divider.first().second

        result.plusToCof(maxDegree, cof)

        for (i in divider)
        {
            Log.d("div@"," degree ${i.first + maxDegree} cof ${i.second * cof}")
            division.minusToCof(i.first + maxDegree, i.second * cof)
        }

        var counter = 0
        for(i in 0 until 100000000)
        {
            counter ++
        }

        division = division.filter { s -> s.second != ComplexNumber() }.toMutableList()
    }
    return Pair(result, division)

}

fun MutableList<Pair<Int, ComplexNumber>>.String(): String
{
    var result = ""
    this.map {
        result += it.second.toString() + "x^" + it.first.toString().toDegree() + "+"
    }
    return result
}