package com.dev.smurf.highmathcalculator.Exceptions

import java.util.*



open class TimeableException(var time : GregorianCalendar = zeroTime) : Exception()
{
    companion object
    {
        val zeroTime : GregorianCalendar
        init
        {
            zeroTime = GregorianCalendar()
            zeroTime.timeInMillis = 0
        }
    }
}