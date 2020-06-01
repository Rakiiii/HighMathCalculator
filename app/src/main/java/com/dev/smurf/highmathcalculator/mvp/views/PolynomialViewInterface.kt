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
    fun setRecyclerViewList(ar : MutableList<PolynomialGroup>)

    //set @position btn fragment
    @StateStrategyType(SkipStrategy::class)
    fun setBtnFragment(position : Int)

    @StateStrategyType(SkipStrategy::class)
    fun setImageAdapter()

    @StateStrategyType(SkipStrategy::class)
    fun setTxtAdapter()

    @StateStrategyType(SkipStrategy::class)
    fun restoreFromViewModel()


    @StateStrategyType(SkipStrategy::class)
    fun saveRecyclerViewToViewModel()

    @StateStrategyType(SkipStrategy::class)
    fun setObserver()
}