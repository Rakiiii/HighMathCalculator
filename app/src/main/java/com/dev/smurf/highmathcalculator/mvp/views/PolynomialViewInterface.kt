package com.dev.smurf.highmathcalculator.mvp.views


import android.graphics.Bitmap
import com.dev.smurf.highmathcalculator.ui.POJO.MatrixGroup
import com.example.smurf.mtarixcalc.PolynomialGroup
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SkipStrategy::class)
interface PolynomialViewInterface : MvpView
{
    //добавить в recycler view с полиномами новый элемент
    @StateStrategyType(SkipStrategy::class)
    fun addToPolynomialRecyclerView(obj: PolynomialGroup)

    //вывести что-то в тост
    @StateStrategyType(SkipStrategy::class)
    fun showToast(obj: String)

    //установить новый список элементов RecyclerView
    @StateStrategyType(OneExecutionStateStrategy::class)
    fun setRecyclerViewList(ar: MutableList<PolynomialGroup>)

    //set @position btn fragment
    @StateStrategyType(SkipStrategy::class)
    fun setBtnFragment(position: Int)

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
    fun clearRecyclerView()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setTopPosition()

    @StateStrategyType(SkipStrategy::class)
    fun displayError(message : String)

    /*
    *   Calculation animation
    */

    @StateStrategyType(SkipStrategy::class)
    fun startCalculation(polynomialGroup: PolynomialGroup)

    @StateStrategyType(SkipStrategy::class)
    fun calculationCompleted(polynomialGroup: PolynomialGroup)

    @StateStrategyType(SkipStrategy::class)
    fun calculationFailed(polynomialGroup: PolynomialGroup)

    @StateStrategyType(SkipStrategy::class)
    fun stopAllCalculations()

    /*
     * extra info dialog
     */

    @StateStrategyType(SkipStrategy::class)
    fun showPolynomialDialog(polynomial: String, width: Float, height: Float, matrixBitmap: Bitmap)

    @StateStrategyType(SkipStrategy::class)
    fun dismissPolynomialDialog()
}