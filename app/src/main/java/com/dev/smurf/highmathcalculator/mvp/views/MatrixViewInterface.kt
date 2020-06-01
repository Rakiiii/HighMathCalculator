package com.dev.smurf.highmathcalculator.mvp.views

import android.graphics.Matrix
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndStrategy::class)
interface MatrixViewInterface : MvpView
{
    //добавить в recycler view новый элемент obj
    @StateStrategyType(SkipStrategy::class)
    fun addToRecyclerView(obj : MatrixGroup)

    //вывести что-то в тост
    @StateStrategyType(SkipStrategy::class)
    fun showToast(obj : String)

    //установить новый список в Recycler view
    @StateStrategyType(SkipStrategy::class)
    fun setRecyclerViewList(ar : MutableList<MatrixGroup>)

    @StateStrategyType(SkipStrategy::class)
    fun setBtnFragment(position : Int)

   /* @StateStrategyType(SkipStrategy::class)
    fun updateSettings()*/

   //установить Image адаптер
    @StateStrategyType(SkipStrategy::class)
    fun setImageAdapter()

    //установить Text адаптер
    @StateStrategyType(SkipStrategy::class)
    fun setTextAdapter()

    @StateStrategyType(SkipStrategy::class)
    fun saveListRecyclerViewViewModel()

    @StateStrategyType(SkipStrategy::class)
    fun restoreFromViewModel()
}