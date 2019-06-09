package com.dev.smurf.highmathcalculator.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface

@InjectViewState
class MainPresenter : MvpPresenter<MainViewInterface>()
{
    fun setMatrixFragment()
    {
        viewState.setMatrixFragment()
    }

    fun setPolinonFragment()
    {
        viewState.setPolinomFragment()
    }

}