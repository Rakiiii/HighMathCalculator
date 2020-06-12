package com.dev.smurf.highmathcalculator.mvp.views

import android.graphics.Bitmap
import android.graphics.Matrix
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import moxy.MvpView
import moxy.viewstate.strategy.*

@StateStrategyType(value = AddToEndStrategy::class)
interface MatrixViewInterface : MvpView
{
    //добавить в recycler view новый элемент obj
    @StateStrategyType(SkipStrategy::class)
    fun addToRecyclerView(obj: MatrixGroup)

    //вывести что-то в тост
    @StateStrategyType(SkipStrategy::class)
    fun showToast(obj: String)

    //установить новый список в Recycler view
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setRecyclerViewList(ar: MutableList<MatrixGroup>)

    @StateStrategyType(SkipStrategy::class)
    fun setBtnFragment(position: Int)

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

    @StateStrategyType(SkipStrategy::class)
    fun setObservable()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showErrorDialog(errorBitmap: Bitmap, width: Float, height: Float, errorText: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun dismissErrorDialog()

    @StateStrategyType(SkipStrategy::class)
    fun getMaxSizeOfErrorDialog()


    @StateStrategyType(SkipStrategy::class)
    fun startLoadingInRecyclerView()

    @StateStrategyType(SkipStrategy::class)
    fun stopLoadingInRecyclerView()

    @StateStrategyType(SkipStrategy::class)
    fun startCalculation(matrixGroup: MatrixGroup)

    @StateStrategyType(SkipStrategy::class)
    fun calculationCompleted(matrixGroup: MatrixGroup)

    @StateStrategyType(SkipStrategy::class)
    fun calculationFailed(matrixGroup: MatrixGroup)

    @StateStrategyType(SkipStrategy::class)
    fun stopAllCalculations()

    @StateStrategyType(SkipStrategy::class)
    fun clearRecyclerView()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTopPosition()

    @StateStrategyType(SkipStrategy::class)
    fun displayError(message : String)
}