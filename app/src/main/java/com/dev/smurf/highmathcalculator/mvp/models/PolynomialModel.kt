package com.dev.smurf.highmathcalculator.mvp.models

import com.dev.smurf.highmathcalculator.Polynomials.PolynomialBase
import com.dev.smurf.highmathcalculator.Polynomials.PolynomialFactory
import com.example.smurf.mtarixcalc.PolynomialGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext

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
    private fun plus(left: String, right: String): PolynomialGroup
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
    private fun minus(left: String, right: String): PolynomialGroup
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

    private fun times(left: String, right: String): PolynomialGroup
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

    private fun division(left: String, right: String): PolynomialGroup
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

    suspend fun PolynomialPlus(coroutineScope: CoroutineScope,left: String, right: String ) : PolynomialGroup
    {
        return withContext(coroutineScope.coroutineContext+Dispatchers.Default)
        {
           plus(left,right)
        }
    }

    suspend fun PolynomialMinus(coroutineScope: CoroutineScope,left: String, right: String ) : PolynomialGroup
    {
        return withContext(coroutineScope.coroutineContext+Dispatchers.Default)
        {
            minus(left,right)
        }
    }

    suspend fun PolynomialTimes(coroutineScope: CoroutineScope,left: String, right: String ) : PolynomialGroup
    {
        return withContext(coroutineScope.coroutineContext+Dispatchers.Default)
        {
            times(left,right)
        }
    }

    suspend fun PolynomialDivision(coroutineScope: CoroutineScope,left: String, right: String ) : PolynomialGroup
    {
        return withContext(coroutineScope.coroutineContext+Dispatchers.Default)
        {
            division(left,right)
        }
    }


    //решение уравнений описываемых полиномом
    fun getSolved(obj: String): PolynomialGroup
    {
        throw Exception("Not implemented yet")
    }

}