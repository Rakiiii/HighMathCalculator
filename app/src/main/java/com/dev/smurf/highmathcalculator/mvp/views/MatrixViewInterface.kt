package com.dev.smurf.highmathcalculator.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.smurf.mtarixcalc.MatrixGroup

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
    fun setRecyclerViewArrayList(ar : ArrayList<MatrixGroup>)

   /* //установить Image адаптер
    @StateStrategyType(SkipStrategy::class)
    fun setImageAdapter()

    //установить Text адаптер
    @StateStrategyType(SkipStrategy::class)
    fun setTextAdapter()*/

}