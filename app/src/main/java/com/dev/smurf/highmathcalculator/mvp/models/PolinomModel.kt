package com.dev.smurf.highmathcalculator.mvp.models

import com.dev.smurf.highmathcalculator.Polinoms.DiofantPolinom
import com.dev.smurf.highmathcalculator.Polinoms.ExponensialPolinom
import com.dev.smurf.highmathcalculator.Polinoms.PolinomBase
import com.dev.smurf.highmathcalculator.Polinoms.PolinomFactory
import com.example.smurf.mtarixcalc.PolinomGroup

class PolinomModel
{
    private var mPolinomFactory = PolinomFactory()

    fun createPolinom(obj : String) : PolinomBase
    {
        return mPolinomFactory.createPolinom(obj)
    }

    fun plus( left : String , right : String) : PolinomGroup
    {
        var lp = mPolinomFactory.createPolinom(left)
        var rp = mPolinomFactory.createPolinom(right)
        if( (lp is DiofantPolinom && rp is DiofantPolinom) || (lp is ExponensialPolinom && rp is ExponensialPolinom) )
        {
            var result: DiofantPolinom = (lp + rp) as DiofantPolinom
            return PolinomGroup(polLeftPolinom = lp , polRightPolinom = rp , polSignPolinom = "+" , polResPolinom = result)
        }
        else
        {
            throw Exception("DifferentPolinomTypes")
        }
    }

    fun minus( left : String , right : String) : PolinomGroup
    {
        var lp = mPolinomFactory.createPolinom(left)
        var rp = mPolinomFactory.createPolinom(right)
        if( (lp is DiofantPolinom && rp is DiofantPolinom) || (lp is ExponensialPolinom && rp is ExponensialPolinom) )
        {
            var result: DiofantPolinom = (lp - rp) as DiofantPolinom
            return PolinomGroup(polLeftPolinom = lp , polRightPolinom = rp , polSignPolinom = "+" , polResPolinom = result)
        }
        else
        {
            throw Exception("DifferentPolinomTypes")
        }
    }

    fun times( left : String , right : String) : PolinomGroup
    {
        var lp = mPolinomFactory.createPolinom(left)
        var rp = mPolinomFactory.createPolinom(right)
        if( (lp is DiofantPolinom && rp is DiofantPolinom) || (lp is ExponensialPolinom && rp is ExponensialPolinom) )
        {
            throw Exception("No times operation for diofant polinoms")
        }
        else
        {
            throw Exception("DifferentPolinomTypes")
        }
    }

    fun division( left : String , right : String) : PolinomGroup
    {
        var lp = mPolinomFactory.createPolinom(left)
        var rp = mPolinomFactory.createPolinom(right)
        if( (lp is DiofantPolinom && rp is DiofantPolinom) || (lp is ExponensialPolinom && rp is ExponensialPolinom) )
        {
            throw Exception("No division operation for diofant polinoms")
        }
        else
        {
            throw Exception("DifferentPolinomTypes")
        }
    }

    fun getSolved(obj : String) : PolinomGroup
    {
        var pol = mPolinomFactory.createPolinom(obj)

        pol.solve()
        var tmp = pol.stringWithRoots()
        return PolinomGroup(polLeftPolinom = pol ,
            polRightPolinom = null ,
            polResPolinom = null ,
            polSignPolinom = "solution" ,
            polOstPolinom = null ,
            isRoots = true , roots = tmp)
    }

}