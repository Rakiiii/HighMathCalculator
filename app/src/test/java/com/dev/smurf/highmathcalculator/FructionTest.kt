package com.dev.smurf.highmathcalculator

import com.dev.smurf.highmathcalculator.Numbers.Fraction
import junit.framework.Assert.assertEquals
import org.junit.Test

class FructionTest
{
    @Test
    fun testIsDecimal()
    {
        assertEquals(Fraction(_upper = 213412,_lower=298347).isDecimal(),false)
        assertEquals(Fraction(_upper = 213412,_lower=5).isDecimal(),false)
        assertEquals(Fraction(_upper = 213412,_lower=100000).isDecimal(),true)
        assertEquals(Fraction(_upper = 213412,_lower=10).isDecimal(),true)
        assertEquals(Fraction(_upper = 213412,_lower=1).isDecimal(),true)
        assertEquals(Fraction(_upper = 213412,_lower=0).isDecimal(),true)
    }
}