package com.dev.smurf.highmathcalculator.mvp.views


import com.example.smurf.mtarixcalc.PolynomialGroup
import moxy.MvpView
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

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