package com.dev.smurf.highmathcalculator.mvp.models

import com.dev.smurf.highmathcalculator.Polinoms.DiofantPolinom
import com.dev.smurf.highmathcalculator.Polinoms.ExponensialPolinom
import com.dev.smurf.highmathcalculator.Polinoms.PolinomBase
import com.dev.smurf.highmathcalculator.Polinoms.PolinomFactory
import com.example.smurf.mtarixcalc.PolinomGroup

class PolinomModel
{

    //фабрика для Polinom
    private var mPolinomFactory = PolinomFactory()


    //функция создания полинома
    fun createPolinom(obj : String) : PolinomBase
    {
        return mPolinomFactory.createPolinom(obj)
    }

    //сложить два полинома
    fun plus( left : String , right : String) : PolinomGroup
    {
        var lp = mPolinomFactory.createPolinom(left)
        var rp = mPolinomFactory.createPolinom(right)

        //если полиномы одного типа то складываем
        if( (lp is DiofantPolinom && rp is DiofantPolinom) || (lp is ExponensialPolinom && rp is ExponensialPolinom) )
        {
            var result: DiofantPolinom = (lp + rp) as DiofantPolinom
            return PolinomGroup(polLeftPolinom = lp , polRightPolinom = rp , polSignPolinom = "+" , polResPolinom = result)
        }
        //иначе кидаем исключение
        else
        {
            throw Exception("DifferentPolinomTypes")
        }
    }


    //выситание полиномов
    fun minus( left : String , right : String) : PolinomGroup
    {
        var lp = mPolinomFactory.createPolinom(left)
        var rp = mPolinomFactory.createPolinom(right)

        //если полиновы одного типа то вычитаем
        if( (lp is DiofantPolinom && rp is DiofantPolinom) || (lp is ExponensialPolinom && rp is ExponensialPolinom) )
        {
            var result: DiofantPolinom = (lp - rp) as DiofantPolinom
            return PolinomGroup(polLeftPolinom = lp , polRightPolinom = rp , polSignPolinom = "+" , polResPolinom = result)
        }
        //иначе исключение
        else
        {
            throw Exception("DifferentPolinomTypes")
        }
    }

    fun times( left : String , right : String) : PolinomGroup
    {
        var lp = mPolinomFactory.createPolinom(left)
        var rp = mPolinomFactory.createPolinom(right)

        //TODO: добавитьь реализацию умножения полиномов
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

        //TODO: добавить реализацию деления полиномов
        if( (lp is DiofantPolinom && rp is DiofantPolinom) || (lp is ExponensialPolinom && rp is ExponensialPolinom) )
        {
            throw Exception("No division operation for diofant polinoms")
        }
        else
        {
            throw Exception("DifferentPolinomTypes")
        }
    }


    //решение уравнений описываемых полиномом
    fun getSolved(obj : String) : PolinomGroup
    {
        var pol = mPolinomFactory.createPolinom(obj)

        //решаем полином
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