package com.dev.smurf.highmathcalculator.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.smurf.mtarixcalc.polGroup

@StateStrategyType(value = SkipStrategy::class)
interface PolinomViewInterface : MvpView
{
    //добавить в recycler view с полиномами новый элемент
    fun addToPolinomRecyclerView( obj : polGroup)
}