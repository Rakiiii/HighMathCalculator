package com.dev.smurf.highmathcalculator.mvp.views

import moxy.MvpView
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

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

    //вкл режим работы на картинках
    fun setHolderImageModeOn()

    //выкл режим работы на картинках
    fun setHolderImageModeOff()

    fun dismissDialog()

    fun showToast(msg : String)

}