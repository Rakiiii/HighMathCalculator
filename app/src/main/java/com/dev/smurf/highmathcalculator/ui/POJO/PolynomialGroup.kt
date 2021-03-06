package com.example.smurf.mtarixcalc

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialRoots
import com.dev.smurf.highmathcalculator.RoomConverters.POJOConverter
import java.util.*

@Entity
@TypeConverters(POJOConverter::class)
data class PolynomialGroup(var polLeftPolynomial : PolynomialBase,
                           var polRightPolynomial : PolynomialBase,
                           var polSignPolynomial : String,
                           var polResPolynomial : PolynomialBase,
                           var polOstPolynomial : PolynomialBase = PolynomialBase.EmptyPolynomial,
                           var roots : PolynomialRoots? = null,
                           @PrimaryKey var time : java.util.GregorianCalendar = GregorianCalendar()
)
{
    fun Copy() = PolynomialGroup(
        polLeftPolynomial = polLeftPolynomial,
        polRightPolynomial = polRightPolynomial,
        polSignPolynomial = polSignPolynomial,
        polOstPolynomial = polOstPolynomial,
        polResPolynomial = polResPolynomial,
        time = time,
        roots = roots
    )

    companion object
    {
        val DIVISION ="/"
        val MINUS = "-"
        val PLUS = "+"
        val TIMES = "*"
        val EQUALS="="
        val CALCULATION="calculation"
        val LOADING = "loading"
    }
}