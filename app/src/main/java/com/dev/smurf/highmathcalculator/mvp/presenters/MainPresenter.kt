package com.dev.smurf.highmathcalculator.mvp.presenters

import moxy.InjectViewState
import moxy.MvpPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface

@InjectViewState
class MainPresenter : MvpPresenter<MainViewInterface>()
{

    //установить фрагмент MatrixFragment
    fun setMatrixFragment()
    {
        viewState.setMatrixFragment()
    }

    //установить фрагмент PolynomialFragment
    fun setPolinonFragment()
    {
        viewState.setPolinomFragment()
    }

    //установить фагмент SettingsFragment
    fun setSettingsFragment()
    {
        viewState.setSettingsFragment()
    }

}