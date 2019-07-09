package com.dev.smurf.highmathcalculator.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.smurf.mtarixcalc.MatrixGroup

@StateStrategyType(value = AddToEndStrategy::class)
interface MatrixViewInterface : MvpView
{
    @StateStrategyType(SkipStrategy::class)
    fun addToRecyclerView(obj : MatrixGroup)

    //вывести что-то в тост
    @StateStrategyType(SkipStrategy::class)
    fun showToast(obj : String)

    @StateStrategyType(SkipStrategy::class)
    fun addLoaded(ar : ArrayList<MatrixGroup>)

}