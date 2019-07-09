package com.dev.smurf.highmathcalculator.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface

@InjectViewState
class MainPresenter : MvpPresenter<MainViewInterface>()
{

    //установить фрагмент MatrixFragment
    fun setMatrixFragment()
    {
        viewState.setMatrixFragment()
    }

    //установить фрагмент PolinomFragment
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