package com.dev.smurf.highmathcalculator.mvp.models

import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialFactory
import com.example.smurf.mtarixcalc.PolynomialGroup

class PolynomialModel
{

    //фабрика для Polinom
    private var mPolinomFactory = PolynomialFactory()


    //функция создания полинома
    fun createPolinom(obj: String): PolynomialBase
    {
        return mPolinomFactory.createPolynomial(obj)
    }

    //сложить два полинома
    fun plus(left: String, right: String): PolynomialGroup
    {
        val lp = mPolinomFactory.createPolynomial(left)
        val rp = mPolinomFactory.createPolynomial(right)


        val result = lp + rp

        return PolynomialGroup(
            polLeftPolynomial = lp,
            polRightPolynomial = rp,
            polSignPolynomial = "+",
            polResPolynomial = result
        )



    }


    //выситание полиномов
    fun minus(left: String, right: String): PolynomialGroup
    {
        val lp = mPolinomFactory.createPolynomial(left)
        val rp = mPolinomFactory.createPolynomial(right)


        val result = lp - rp
        return PolynomialGroup(
            polLeftPolynomial = lp,
            polRightPolynomial = rp,
            polSignPolynomial = "-",
            polResPolynomial = result
        )

    }

    fun times(left: String, right: String): PolynomialGroup
    {
        val lp = mPolinomFactory.createPolynomial(left)
        val rp = mPolinomFactory.createPolynomial(right)

        val result = lp * rp

        return PolynomialGroup(
            polLeftPolynomial = lp,
            polRightPolynomial = rp,
            polSignPolynomial = "*",
            polResPolynomial = result
        )
    }

    fun division(left: String, right: String): PolynomialGroup
    {
        val lp = mPolinomFactory.createPolynomial(left)
        val rp = mPolinomFactory.createPolynomial(right)

        val result = lp / rp

        return PolynomialGroup(
            polLeftPolynomial = lp,
            polRightPolynomial = rp,
            polSignPolynomial = "/",
            polResPolynomial = result.first,
            polOstPolynomial = result.second
        )
    }


    //решение уравнений описываемых полиномом
    fun getSolved(obj: String): PolynomialGroup
    {
        throw Exception("Not implemented yet")
    }

}