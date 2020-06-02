package com.dev.smurf.highmathcalculator.mvp.presenters

import com.dev.smurf.highmathcalculator.mvp.views.MainViewInterface
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*

@InjectViewState
class MainPresenter : MvpPresenter<MainViewInterface>()
{

    private val backStack = Stack<Int>()

    //установить фрагмент MatrixFragment
    fun setMatrixFragment()
    {
        backStack.push(1)
        viewState.setMatrixFragment()
    }

    //установить фрагмент PolynomialFragment
    fun setPolinonFragment()
    {
        backStack.push(0)
        viewState.setPolinomFragment()
    }

    fun onBackPress()
    {
        if(backStack.size < 1)
        {
            viewState.onSuperBack()
            return
        }
        val prevsiousState = backStack.pop()
        when (prevsiousState)
        {
            0 ->
            {
                viewState.setMatrixFragment()
                backStack.pop()
            }
            1 ->
            {
                viewState.setPolinomFragment()
                backStack.pop()
            }
        }
    }

    //установить фагмент SettingsFragment
    fun setSettingsFragment()
    {
        viewState.setSettingsFragment()
    }
}