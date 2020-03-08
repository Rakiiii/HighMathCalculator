package com.dev.smurf.highmathcalculator.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.smurf.mtarixcalc.PolynomialGroup

@StateStrategyType(value = SkipStrategy::class)
interface PolynomialViewInterface : MvpView
{
    //добавить в recycler view с полиномами новый элемент
    fun addToPolynomialRecyclerView(obj : PolynomialGroup)

    //вывести что-то в тост
    fun showToast(obj : String)

    //установить новый список элементов RecyclerView
    fun setRecyclerViewList(ar : ArrayList<PolynomialGroup>)
}