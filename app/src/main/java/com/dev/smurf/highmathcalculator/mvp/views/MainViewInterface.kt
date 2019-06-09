package com.dev.smurf.highmathcalculator.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainViewInterface : MvpView
{

    //установить фрагмент с полиномом
    fun setPolinomFragment()

    //установить фрагмент с матрицами
    fun setMatrixFragment()

}