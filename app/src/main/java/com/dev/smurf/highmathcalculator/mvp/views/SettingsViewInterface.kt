package com.dev.smurf.highmathcalculator.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType( value = SkipStrategy::class)
interface SettingsViewInterface : MvpView
{
    //выкл режим сохранения данных матриц
    fun setMatrixModeOff()

    //вкл режим сохранения матриц
    fun setMatrixModeOn()

    //выкл режим сохранения данных полиномов
    fun setPolinomModeOff()

    //вкл режим сохранения матриц
    fun setPolinomModeOn()

}