package com.dev.smurf.highmathcalculator.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.dev.smurf.highmathcalculator.mvp.views.PolinomViewInterface
import com.example.smurf.mtarixcalc.polGroup

@InjectViewState
class PolinomPresenter : MvpPresenter<PolinomViewInterface>()
{

    //нажатие кнопки плюс
    fun onPlusClick(left : String , right : String ) : polGroup
    {}

    //нажатие на кнопку минус
    fun onMinusClick(left : String , right : String) : polGroup
    {}

    //нажатие на кнопку умножения
    fun onTimesClick(left: String, right: String) : polGroup
    {}

    //нажатие на кнопку деления
    fun onDivisionClick(left: String , right: String) : polGroup
    {}

    //
    fun onRootsOfClick(left : String) : polGroup
    {}

}