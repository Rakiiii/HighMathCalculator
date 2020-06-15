package com.dev.smurf.highmathcalculator.mvp.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainViewInterface : MvpView
{

    //установить фрагмент с полиномом
    fun setPolinomFragment()

    //установить фрагмент с матрицами
    fun setMatrixFragment()

    //установить фрагмент с настройками
    fun setSettingsFragment()

    fun onSuperBack()

}